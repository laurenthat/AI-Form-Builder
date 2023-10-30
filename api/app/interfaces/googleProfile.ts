export interface GoogleProfile {
    provider: string;
    sub: string;
    id: string;
    displayName: string;
    name: {
        givenName: string;
        familyName: string;
    };
    givenName: string;
    familyName: string;
    emailVerified: boolean;
    verified: boolean;
    language: string;
    email: string;
    emails: {
        value: string;
        type: string;
    }[];
    photos: {
        value: string;
        type: string;
    }[];
    picture: string;
    raw: string;
    json: {
        sub: string;
        name: string;
        givenName: string;
        familyName: string;
        picture: string;
        email: string;
        emailVerified: boolean;
        locale: string;
    };
}

export interface GoogleProfileFromApi {
    provider: string;
    sub: string;
    id: string;
    displayName: string;
    name: {
        givenName: string;
        familyName: string;
    };
    given_name: string;
    family_name: string;
    email_verified: boolean;
    verified: boolean;
    language: string;
    email: string;
    emails: {
        value: string;
        type: string;
    }[];
    photos: {
        value: string;
        type: string;
    }[];
    picture: string;
    _raw: string;
    _json: {
        sub: string;
        name: string;
        given_name: string;
        family_name: string;
        picture: string;
        email: string;
        email_verified: boolean;
        locale: string;
    };
}

export function normalizeGoogleProfile(input: GoogleProfileFromApi): GoogleProfile {
    const camelCaseUser: GoogleProfile = {
        provider: input.provider,
        sub: input.sub,
        id: input.id,
        displayName: input.displayName,
        name: {
            givenName: input.name.givenName,
            familyName: input.name.familyName,
        },
        givenName: input.given_name,
        familyName: input.family_name,
        emailVerified: input.email_verified,
        verified: input.verified,
        language: input.language,
        email: input.email,
        emails: input.emails.map((email) => ({
            value: email.value,
            type: email.type,
        })),
        photos: input.photos.map((photo) => ({
            value: photo.value,
            type: photo.type,
        })),
        picture: input.picture,
        raw: input._raw,
        json: {
            sub: input._json.sub,
            name: input._json.name,
            givenName: input._json.given_name,
            familyName: input._json.family_name,
            picture: input._json.picture,
            email: input._json.email,
            emailVerified: input._json.email_verified,
            locale: input._json.locale,
        },
    };

    return camelCaseUser;
}
