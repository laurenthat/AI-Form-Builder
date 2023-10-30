import express from 'express';

export function configParsers(app: any) {
    app.use(express.urlencoded({ extended: false }));
    app.use(express.json());
}
