import { Strategy as GoogleStrategy } from 'passport-google-oauth2';
import passport from 'passport';
import { generateAccessToken } from './passport-jwt.service.js';
import { GoogleProfile, GoogleProfileFromApi, normalizeGoogleProfile } from '../interfaces/googleProfile.js';
import { UserDatabase } from '../databases/userDatabase.js';
import { readFileSync } from 'fs';
import { resolve } from 'path';
import { environment } from '../configurations/environment.js';
import { Express } from 'express';

const continueWithAppTemplate = readFileSync(
    resolve(process.cwd(), 'app', 'templates', 'continue-with-app.html'),
).toString();

export function config() {
    passport.use(
        'google',
        new GoogleStrategy(
            {
                clientID: environment.APP_OAUTH2_CLIENT_ID,
                clientSecret: environment.APP_OAUTH2_CLIENT_SECRET,
                callbackURL: environment.APP_OAUTH2_CALLBACK_URL,
                passReqToCallback: true,
                scope: ['email', 'profile'],
            },
            function (request: any, accessToken: any, refreshToken: any, profile: GoogleProfileFromApi, done: any) {
                const error = null;
                return done(error, normalizeGoogleProfile(profile));
            },
        ),
    );

    passport.serializeUser(function (user, done) {
        done(null, user);
    });

    passport.deserializeUser(function (user: any, done) {
        done(null, user);
    });
}

export function configRoutes(app: Express) {
    app.get('/auth/google', passport.authenticate('google'));

    app.get('/auth/google/callback', passport.authenticate('google'), async (req: any, res: any) => {
        const googleProfile = req.user as GoogleProfile;
        const user = await UserDatabase.syncUserByGoogleProfile(googleProfile);
        const token = generateAccessToken(user);
        const params = new URLSearchParams();
        params.set('token', JSON.stringify(token));
        params.set('user', JSON.stringify(user));
        const template = continueWithAppTemplate.replace('[launchLink]', `/android/auth/login?${params.toString()}`);

        res.send(template);
    });

    app.get('/android/auth/login', (req: any, res: any) => {
        res.send({ success: 'nothing here on web.' });
    });
}
