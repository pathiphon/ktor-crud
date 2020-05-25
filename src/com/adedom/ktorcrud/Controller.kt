package com.adedom.ktorcrud

import io.ktor.routing.Route
import io.ktor.routing.route

fun Route.controller() {

    route("/account"){
        user()
    }

}
