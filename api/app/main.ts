import express, { Request, Response } from 'express';

import {
    config as configGoogleOAuth2,
    configRoutes as configGoogleOAuth2Routes,
} from './strategies/passport-google-oauth2.service.js';

import { config as configJWT, requiresAccessToken } from './strategies/passport-jwt.service.js';
import { configSession } from './configurations/configSession.js';
import { configParsers } from './configurations/configParsers.js';
import { environment } from './configurations/environment.js';
import { Card, ConnectionCard, Tag, User } from '@prisma/client';
import { prisma } from './databases/userDatabase.js';
import { ErrorRequestHandler } from 'express-serve-static-core';
import morgan from 'morgan';
// Express App
const app = express();

app.use(
    morgan(':method :status :url :response-time', {
        skip: function (req, res) {
            return res.statusCode < 400;
        },
    }),
);

// Session is required by passport.js
configSession(app);

// Parsing capabilities for body of request.
configParsers(app);

// https://www.passportjs.org/packages/passport-google-oauth2/
configGoogleOAuth2();

// https://www.passportjs.org/packages/passport-jwt/
configJWT();

// adding required routes for authentication for this strategy
configGoogleOAuth2Routes(app);

/**
 * Required by Android
 * https://www.branch.io/resources/blog/how-to-open-an-android-app-from-the-browser/
 * https://developer.android.com/training/app-links/verify-android-applinks
 */
app.get('/.well-known/assetlinks.json', (req: Request, res: Response) => {
    res.json([
        {
            relation: ['delegate_permission/common.handle_all_urls'],
            target: {
                namespace: 'android_app',
                package_name: 'com.sbma.linkup',
                sha256_cert_fingerprints: environment.APP_ANDROID_SHA256_CERT_FINGERPRINT.split(','),
            },
        },
    ]);
});

app.get('/profile', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const response = await prisma.user.findFirst({
        where: {
            id: (req.user as User).id,
        },
        include: {
            cards: true,
            connections: {
                include: {
                    user: true,
                    connectedUser: true,
                    connectionCards: {
                        include: {
                            card: true,
                        },
                    },
                },
            },
            reverseConnections: {
                include: {
                    user: true,
                    connectionCards: {
                        include: {
                            card: true,
                        },
                    },
                },
            },
            shares: {
                include: {
                    tags: true,
                    cards: true,
                },
            },
        },
    });
    res.status(200).send(response);
    return;
});

app.put('/profile', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const body: User = req.body as User;
    const response = await prisma.user.update({
        where: {
            id: user.id,
        },
        data: {
            ...user,
            ...body,
            id: user.id,
            email: user.email,
        },
    });

    res.status(200).send(response);
    return;
});

app.post('/card', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const body: Card = req.body as Card;
    const response = await prisma.card.create({
        data: {
            ownerId: user.id,
            title: body.title.trim(),
            value: body.value.trim(),
            picture: body.picture?.trim() ?? null,
        },
    });
    res.status(200).send(response);
    return;
});
app.put('/card/:id', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const id: string = req.params.id as string;
    const body: Card = req.body as Card;
    const card = await prisma.card.findFirst({
        where: {
            id,
            ownerId: user.id,
        },
    });
    if (card === null) {
        res.status(400).send({
            message: 'Not Found.',
        });
        return;
    }
    const response = await prisma.card.update({
        where: {
            id: card.id,
        },
        data: {
            title: body.title ?? card.title,
            value: body.value ?? card.value,
            picture: body.picture ?? card.picture,
        },
    });
    res.status(200).send(response);
    return;
});
app.delete('/card/:id', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const id: string = req.params.id as string;
    const response = await prisma.card.delete({
        where: {
            id: id,
            ownerId: user.id,
        },
    });
    res.status(200).send(response);
    return;
});

app.post('/share', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const body: string[] = req.body as string[];

    const share = await prisma.share.create({
        data: {
            userId: user.id,
        },
    });
    const shareCards = await Promise.all(
        body.map(async (cardId) => {
            // Only user's own card
            await prisma.card.findFirstOrThrow({
                where: {
                    id: cardId,
                    ownerId: user.id,
                },
            });
            return prisma.shareCard.create({
                data: {
                    cardId: cardId,
                    shareId: share.id,
                },
            });
        }),
    );

    const response = await prisma.share.findFirstOrThrow({
        where: {
            id: share.id,
        },
        include: {
            tags: true,
            cards: true,
            user: true,
        },
    });

    res.status(200).send(response);
    return;
});

/**
 *
 * @param user User who scans the code.
 * @param shareId User who shared their QR code or card.
 * @param res this function is responsible to respond.
 */
async function onScan(user: User, shareId: string, res: Response): Promise<void> {
    const share = await prisma.share.findFirst({
        where: {
            id: shareId,
        },
        include: {
            tags: true,
            cards: true,
        },
    });
    if (share === null) {
        res.status(400).send({
            message: 'Not Found.',
        });
        return;
    }
    const shareCards = await prisma.shareCard.findMany({
        where: {
            shareId: share.id,
        },
    });
    if (share.userId === user.id) {
        res.status(400).send(
            "Looks like you've fallen into a QR code loop! We'll break the cycle for you - scan someone else's code to escape the matrix!",
        );
        return;
    }
    const existingConnection = await prisma.connection.findFirst({
        where: {
            userId: user.id,
            connectedUserId: share.userId,
        },
    });
    const connection =
        existingConnection ??
        (await prisma.connection.create({
            data: {
                userId: user.id,
                connectedUserId: share.userId,
            },
        }));

    const connectionCards = await Promise.all(
        shareCards.map(async (connectCard) => {
            const card: ConnectionCard | null = await prisma.connectionCard.findFirst({
                where: {
                    cardId: connectCard.cardId,
                    connectionId: connection.id,
                },
            });
            return card
                ? new Promise<ConnectionCard>((resolve) => resolve(card))
                : prisma.connectionCard.create({
                      data: {
                          cardId: connectCard.cardId,
                          connectionId: connection.id,
                      },
                  });
        }),
    );
    const response = await prisma.connection.findFirstOrThrow({
        where: {
            id: connection.id,
        },
        include: {
            connectionCards: true,
        },
    });
    res.status(200).send(response);
}

app.post('/share/:id/scan', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const id = req.params.id.trim();

    await onScan(user, id, res);
});

app.post('/tag', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const body: Tag = req.body as Tag;
    const response = await prisma.tag.create({
        data: {
            shareId: body.shareId,
            tagId: body.tagId,
        },
    });
    res.status(200).send(response);
    return;
});

app.post('/tag/:id/scan', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const id = req.params.id;

    const response = await prisma.tag.findFirst({
        where: {
            tagId: id,
        },
    });
    if (response === null) {
        res.status(400).send({
            message: 'Not Found.',
        });
        return;
    }
    await onScan(user, response.shareId, res);
});

app.delete('/tag/:id', requiresAccessToken, async (req: Request, res: Response): Promise<void> => {
    const user: User = req.user as User;
    const id = req.params.id;

    const tag = await prisma.tag.findFirst({
        where: {
            id: id,
        },
        include: {
            share: true,
        },
    });
    if (tag === null) {
        res.status(400).send({
            message: 'Not Found.',
        });
        return;
    }
    if (tag.share.userId !== user.id) {
        res.status(400).send({
            message: 'This tag is not available.',
        });
        return;
    }

    const response = await prisma.tag.delete({
        where: {
            id,
        },
    });
    res.status(200).send(response);
    return;
});

// Define a global error handler middleware
app.use((async (err, req: Request, res: Response, next): Promise<void> => {
    // Handle the error here
    console.error(err);

    // Set an appropriate status code for the error
    res.status(500);

    // Send a JSON response with the error message
    res.json({
        error: 'Internal Server Error',
        message: err.message,
    });
}) satisfies ErrorRequestHandler<any>);

app.listen(8000, '0.0.0.0', (): void => {
    console.log('Started listening on port 8000');
});
