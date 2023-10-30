package com.sbma.linkup.api.apimodels

import com.sbma.linkup.card.Card
import com.sbma.linkup.connection.Connection
import com.sbma.linkup.connection.ConnectionCard
import com.sbma.linkup.user.User
import java.util.UUID

data class ApiUser(
    val id: String,
    val email: String,
    val name: String,
    val picture: String?,
    val cards: List<ApiCard>?,
    val connections: List<ApiConnection>?,
    val reverseConnections: List<ApiConnection>?,
    val shares: List<ApiShare>?
)
fun ApiUser.toUser(): User = User(UUID.fromString(id), name, email ,"", picture)
fun List<ApiUser>.toUserList(): List<User> = this.map { it.toUser() }


data class ApiCard(
    val id: String,
    val title: String,
    val value: String,
    val picture: String?,
    val ownerId: String,
    val owner: ApiUser?,
    val connectionCards: List<ApiConnectionCard>?,
    val shareCards: List<ApiShareCard>?
)
fun ApiCard.toCard(): Card = Card(UUID.fromString(id), UUID.fromString(ownerId), title, value, picture)
fun Card.toApiCard(): ApiCard = ApiCard(id = id.toString(), ownerId = userId.toString(), title = title, value = value, picture = picture, owner = null, connectionCards = null, shareCards = null)
fun List<ApiCard>.toCardList(): List<Card> = this.map { it.toCard() }

data class ApiConnection(
    val id: String,
    val userId: String,
    val user: ApiUser?,
    val connectedUserId: String,
    val connectedUser: ApiUser?,
    val connectionCards: List<ApiConnectionCard>?
)
fun ApiConnection.toConnection(): Connection = Connection(UUID.fromString(id), UUID.fromString(userId), UUID.fromString(connectedUserId))
fun List<ApiConnection>.toConnectionList(): List<Connection> = this.map { it.toConnection() }

data class ApiConnectionCard(
    val id: String,
    val cardId: String,
    val card: ApiCard?,
    val connectionId: String,
    val connection: ApiConnection?
)

fun ApiConnectionCard.toConnectionCard(): ConnectionCard = ConnectionCard(UUID.fromString(id), UUID.fromString(cardId), UUID.fromString(connectionId))
fun List<ApiConnectionCard>.toConnectionCardList(): List<ConnectionCard> = this.map { it.toConnectionCard() }


data class ApiShare(
    val id: String,
    val userId: String,
    val user: ApiUser?,
    val cards: List<ApiShareCard>?,
    val tags: List<ApiTag>?
)

data class ApiShareCard(
    val id: String,
    val shareId: String,
    val share: ApiShare?,
    val cardId: String,
    val card: ApiCard?
)

data class ApiTag(
    val id: String,
    val shareId: String,
    val share: ApiShare?,
    val tagId: String
)

data class AssignTagRequest (
    val shareId: String,
    val tagId: String
)