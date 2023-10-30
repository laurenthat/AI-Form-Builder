import { cleanEnv, host, str } from 'envalid';
import { config } from 'dotenv';

// Load environment variables from a file named .env
config();
export const environment = cleanEnv(process.env, {
    APP_OAUTH2_CLIENT_ID: str(),
    APP_OAUTH2_CLIENT_SECRET: str(),
    APP_OAUTH2_CALLBACK_URL: str(),
    APP_JWT_SECRET: str(),
    APP_JWT_ISSUER: host(),
    APP_JWT_AUDIENCE: host(),
    APP_SESSION_SECRET: str(),
    APP_ANDROID_SHA256_CERT_FINGERPRINT: str(),
});
