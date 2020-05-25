package com.adedom.ktorcrud

import com.adedom.ktorcrud.models.*
import io.ktor.application.call
import io.ktor.request.receive
import io.ktor.request.receiveParameters
import io.ktor.response.respond
import io.ktor.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun Route.user() {

    route("/user") {

        get("/") {
//            val users = transaction {
//                Users.selectAll().orderBy(Users.userId to SortOrder.DESC)
//                    .map { Users.toUser(it) }
//            }

            val sql = "SELECT * FROM user"
            val users = sql.selectAll {
                User(
                    userId = it.getInt(1),
                    name = it.getString(2),
                    username = it.getString(3),
                    password = it.getString(4)
                )
            }

            val response = UsersResponse(
                success = true,
                message = "Fetch users success",
                users = users
            )
            call.respond(response)
        }

        get("/{user_id}") {
            val response = UserResponse()
            val userId = call.parameters["user_id"]
            when {
                userId.isNullOrBlank() -> response.message = "Please enter user id"
                userId.toInt() <= 0 -> response.message = "Please enter a valid user id"
                else -> {
//                    val user = transaction {
//                        Users.select { Users.userId eq userId.toInt() }.map { Users.toUser(it) }.single()
//                    }

                    val sql = "SELECT * FROM user WHERE user_id = '$userId'"
                    val user = sql.select {
                        User(
                            userId = it.getInt(1),
                            name = it.getString(2),
                            username = it.getString(3),
                            password = it.getString(4)
                        )
                    }

                    response.success = true
                    response.message = "Fetch user success"
                    response.user = user
                }
            }
            call.respond(response)
        }

        get("/group") {
            val users = transaction {
                Users.selectAll().groupBy(Users.name).map { Users.toUser(it) }
            }
            val response = UsersResponse(
                success = true,
                message = "Group success",
                users = users
            )
            call.respond(response)
        }

        post("/") {
            val (_, _name, _username, _password) = call.receive<User>()
            val response = BaseResponse()

            when {
                _name.isNullOrBlank() -> response.message = "Please enter name"
                _username.isNullOrBlank() -> response.message = "Please enter username"
                _password.isNullOrBlank() -> response.message = "Please enter password"
                validate { Users.name eq _name } -> response.message = "Please enter other name"
                validate { Users.username eq _username } -> response.message = "Please enter other username"
                else -> {
                    transaction {
                        Users.insert {
                            it[name] = _name
                            it[username] = _username
                            it[password] = _password
                        }
                    }
                    response.success = true
                    response.message = "Insert user success"
                }
            }
            call.respond(response)
        }

        put("/") {
            val parameters = call.receiveParameters()
            transaction {
                Users.update({ Users.userId eq parameters["user_id"]!!.toInt() }) {
                    it[name] = parameters["name"]!!
                    it[username] = parameters["username"]!!
                    it[password] = parameters["password"]!!
                }
            }
            val response = BaseResponse(
                success = true,
                message = "Update user success"
            )
            call.respond(response)
        }

        delete("/{user_id}") {
            val userId = call.parameters["user_id"]!!.toInt()
            transaction {
                Users.deleteWhere {
                    Users.userId eq userId
                }
            }
            val response = BaseResponse(
                success = true,
                message = "Delete user success"
            )
            call.respond(response)
        }

    }

}

private fun validate(where: SqlExpressionBuilder.() -> Op<Boolean>): Boolean {
    val count = transaction {
        Users.select { SqlExpressionBuilder.where() }.count().toInt()
    }
    return count > 0
}
