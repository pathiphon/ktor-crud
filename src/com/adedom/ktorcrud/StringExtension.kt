package com.adedom.ktorcrud

import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.ResultSet

@Suppress("UNCHECKED_CAST")
fun <T : Any> String.select(transform: (ResultSet) -> T): T {
    var result = Any() as T
    transaction {
        TransactionManager.current().exec(this@select) { rs ->
            while (rs.next()) {
                result = transform(rs)
            }
        }
    }
    return result
}

fun <T : Any> String.selectAll(transform: (ResultSet) -> T): List<T> {
    val result = arrayListOf<T>()
    transaction {
        TransactionManager.current().exec(this@selectAll) { rs ->
            while (rs.next()) {
                result += transform(rs)
            }
        }
    }
    return result
}
