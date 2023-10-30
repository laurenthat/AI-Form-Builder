/*
  Warnings:

  - You are about to drop the `UserGoogleProfile` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE "UserGoogleProfile" DROP CONSTRAINT "UserGoogleProfile_userId_fkey";

-- DropTable
DROP TABLE "UserGoogleProfile";
