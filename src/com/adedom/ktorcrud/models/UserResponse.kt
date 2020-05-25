package com.adedom.ktorcrud.models

data class UserResponse(
    var success: Boolean = false,
    var message: String = "Error",
    var user: User? = null
)
