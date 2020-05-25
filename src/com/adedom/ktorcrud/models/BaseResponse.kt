package com.adedom.ktorcrud.models

data class BaseResponse(
    var success: Boolean = false,
    var message: String? = null
)
