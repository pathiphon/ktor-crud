package com.adedom.ktorcrud

import com.adedom.ktorcrud.models.User
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users : Table(name = "user") {
    val userId = Users.integer(name = "user_id").autoIncrement()
    val name = Users.varchar(name = "name", length = 100)
    val username = Users.varchar(name = "username", length = 100)
    val password = Users.varchar(name = "password", length = 100)

    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(userId, name = "PK_User_ID")

    fun toUser(row: ResultRow) = User(
        userId = row[userId],
        name = row[name],
        username = row[username],
        password = row[password]
    )

}