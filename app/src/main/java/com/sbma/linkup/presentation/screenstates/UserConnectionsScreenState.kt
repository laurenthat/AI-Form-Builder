package com.sbma.linkup.presentation.screenstates

import com.sbma.linkup.connection.Connection
import com.sbma.linkup.user.User

data class UserConnectionsScreenState(
    val connections: Map<Connection, User>
)