package com.vnapnic.p2p.model

import org.springframework.web.socket.WebSocketSession

data class Room(
        var id: Long? = null,
        var clients: MutableMap<String?, WebSocketSession>? = null
)