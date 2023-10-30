import { GoogleProfile } from '../interfaces/googleProfile.js';
import { PrismaClient, User } from '@prisma/client';

export const prisma = new PrismaClient();

async function syncUserByGoogleProfile(googleProfile: GoogleProfile): Promise<User> {
    const user = await prisma.user.findFirst({
        where: {
            email: googleProfile.email,
        },
    });

    if (!user) {
        return prisma.user.create({
            data: {
                email: googleProfile.email,
                name: googleProfile.displayName,
                picture: googleProfile.picture,
            },
        });
    }
    const updatedUser = await prisma.user.update({
        where: {
            id: user.id,
        },
        data: {
            picture: user.picture ?? googleProfile.picture,
        },
    });

    return updatedUser;
}

export const UserDatabase = {
    syncUserByGoogleProfile: syncUserByGoogleProfile,
};
