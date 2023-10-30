/*
  Warnings:

  - You are about to drop the column `connectId` on the `Tag` table. All the data in the column will be lost.
  - A unique constraint covering the columns `[shareId,tagId]` on the table `Tag` will be added. If there are existing duplicate values, this will fail.
  - Added the required column `shareId` to the `Tag` table without a default value. This is not possible if the table is not empty.

*/
-- DropForeignKey
ALTER TABLE "Tag" DROP CONSTRAINT "Tag_connectId_fkey";

-- DropIndex
DROP INDEX "Tag_connectId_tagId_key";

-- AlterTable
ALTER TABLE "Tag" DROP COLUMN "connectId",
ADD COLUMN     "shareId" TEXT NOT NULL;

-- CreateIndex
CREATE UNIQUE INDEX "Tag_shareId_tagId_key" ON "Tag"("shareId", "tagId");

-- AddForeignKey
ALTER TABLE "Tag" ADD CONSTRAINT "Tag_shareId_fkey" FOREIGN KEY ("shareId") REFERENCES "Share"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
