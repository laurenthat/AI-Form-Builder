/*
  Warnings:

  - You are about to drop the column `title` on the `Connection` table. All the data in the column will be lost.
  - You are about to drop the column `value` on the `Connection` table. All the data in the column will be lost.

*/
-- AlterTable
ALTER TABLE "Connection" DROP COLUMN "title",
DROP COLUMN "value";
