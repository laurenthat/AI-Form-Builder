-- CreateTable
CREATE TABLE "User" (
    "id" TEXT NOT NULL,
    "email" TEXT NOT NULL,
    "name" TEXT NOT NULL,
    "userGoogleProfileId" TEXT NOT NULL,

    CONSTRAINT "User_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "UserGoogleProfile" (
    "id" TEXT NOT NULL,
    "userId" TEXT NOT NULL,

    CONSTRAINT "UserGoogleProfile_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Card" (
    "id" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "value" TEXT NOT NULL,
    "ownerId" TEXT NOT NULL,

    CONSTRAINT "Card_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Connection" (
    "id" TEXT NOT NULL,
    "title" TEXT NOT NULL,
    "value" TEXT NOT NULL,
    "userId" TEXT NOT NULL,
    "connectedUserId" TEXT NOT NULL,

    CONSTRAINT "Connection_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "ConnectionCard" (
    "id" TEXT NOT NULL,
    "cardId" TEXT NOT NULL,
    "connectionId" TEXT NOT NULL,

    CONSTRAINT "ConnectionCard_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Connect" (
    "id" TEXT NOT NULL,
    "userId" TEXT NOT NULL,

    CONSTRAINT "Connect_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "ConnectCard" (
    "id" TEXT NOT NULL,
    "connectId" TEXT NOT NULL,
    "cardId" TEXT NOT NULL,

    CONSTRAINT "ConnectCard_pkey" PRIMARY KEY ("id")
);

-- CreateTable
CREATE TABLE "Tag" (
    "id" TEXT NOT NULL,
    "connectId" TEXT NOT NULL,
    "tagId" TEXT NOT NULL,

    CONSTRAINT "Tag_pkey" PRIMARY KEY ("id")
);

-- CreateIndex
CREATE UNIQUE INDEX "User_email_key" ON "User"("email");

-- CreateIndex
CREATE UNIQUE INDEX "UserGoogleProfile_userId_key" ON "UserGoogleProfile"("userId");

-- CreateIndex
CREATE UNIQUE INDEX "Connection_userId_connectedUserId_key" ON "Connection"("userId", "connectedUserId");

-- CreateIndex
CREATE UNIQUE INDEX "ConnectionCard_cardId_connectionId_key" ON "ConnectionCard"("cardId", "connectionId");

-- CreateIndex
CREATE UNIQUE INDEX "ConnectCard_cardId_connectId_key" ON "ConnectCard"("cardId", "connectId");

-- CreateIndex
CREATE UNIQUE INDEX "Tag_tagId_key" ON "Tag"("tagId");

-- CreateIndex
CREATE UNIQUE INDEX "Tag_connectId_tagId_key" ON "Tag"("connectId", "tagId");

-- AddForeignKey
ALTER TABLE "UserGoogleProfile" ADD CONSTRAINT "UserGoogleProfile_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Card" ADD CONSTRAINT "Card_ownerId_fkey" FOREIGN KEY ("ownerId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Connection" ADD CONSTRAINT "Connection_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Connection" ADD CONSTRAINT "Connection_connectedUserId_fkey" FOREIGN KEY ("connectedUserId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ConnectionCard" ADD CONSTRAINT "ConnectionCard_cardId_fkey" FOREIGN KEY ("cardId") REFERENCES "Card"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ConnectionCard" ADD CONSTRAINT "ConnectionCard_connectionId_fkey" FOREIGN KEY ("connectionId") REFERENCES "Connection"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Connect" ADD CONSTRAINT "Connect_userId_fkey" FOREIGN KEY ("userId") REFERENCES "User"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ConnectCard" ADD CONSTRAINT "ConnectCard_connectId_fkey" FOREIGN KEY ("connectId") REFERENCES "Connect"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "ConnectCard" ADD CONSTRAINT "ConnectCard_cardId_fkey" FOREIGN KEY ("cardId") REFERENCES "Card"("id") ON DELETE RESTRICT ON UPDATE CASCADE;

-- AddForeignKey
ALTER TABLE "Tag" ADD CONSTRAINT "Tag_connectId_fkey" FOREIGN KEY ("connectId") REFERENCES "Connect"("id") ON DELETE RESTRICT ON UPDATE CASCADE;
