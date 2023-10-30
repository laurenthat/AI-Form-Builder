/*
  Warnings:

  - You are about to drop the `Connect` table. If the table is not empty, all the data it contains will be lost.
  - You are about to drop the `ConnectCard` table. If the table is not empty, all the data it contains will be lost.

*/
-- DropForeignKey
ALTER TABLE "Connect" DROP CONSTRAINT "Connect_userId_fkey";

-- DropForeignKey
ALTER TABLE "ConnectCard" DROP CONSTRAINT "ConnectCard_cardId_fkey";

-- DropForeignKey
ALTER TABLE "ConnectCard" DROP CONSTRAINT "ConnectCard_connectId_fkey";

-- DropForeignKey
ALTER TABLE "Tag" DROP CONSTRAINT "Tag_connectId_fkey";

-- DropTable
DROP TABLE "Connect";

-- DropTable
DROP TABLE "ConnectCard";

-- CreateTable
CREATE TABLE "Share" (
    "id" TEXT NOT NULL,
    "userId" TEXT NOT NULL,

    CONSTRAINT "Share_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "ShareCard" (
    "id" TEXT NOT NULL,
    "shareId" TEXT NOT NULL,
    "cardId" TEXT NOT NULL,

    CONSTRAINT "ShareCard_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "ShareCard_cardId_shareId_key" ON "ShareCard"("cardId", "shareId");

-- AddForeignKey
ALTER TABLE "Share" ADD CONSTRAINT "Share_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ShareCard" ADD CONSTRAINT "ShareCard_shareId_fkey" FOREIGN KEY ("shareId") REFERENCES "Share"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ShareCard" ADD CONSTRAINT "ShareCard_cardId_fkey" FOREIGN KEY ("cardId") REFERENCES "Card"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Tag" ADD CONSTRAINT "Tag_connectId_fkey" FOREIGN KEY ("connectId") REFERENCES "Share"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
