package com.adedom.ktorcrud.models

data class UsersResponse(
    val success: Boolean = false,
    val message: String = "Error",
    val users: List<User>? = null
)
