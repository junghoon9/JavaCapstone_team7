package kr.mjc.junghoon.javacapstonedesign.login

import java.sql.DriverManager
import java.sql.SQLException

class DatabaseHelper {
    private val url = "jdbc:sqlite:users.db"

    init {
        createTable()
    }

    private fun createTable() {
        val sql = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                userID TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            );
        """.trimIndent()

        try {
            DriverManager.getConnection(url).use { conn ->
                conn.createStatement().use { statement ->
                    statement.execute(sql)
                }
            }
        }
        catch (e: SQLException) {
            println("Error creating table: ${e.message}")
        }
    }

    fun insertUser(userID: String, password: String, username: String): Boolean {
        val sql = "INSERT INTO users(userID, password, username) VALUES (?, ?, ?)"
        return try {
            DriverManager.getConnection(url).use { conn ->
                conn.prepareStatement(sql).use { prepareStatement ->
                    prepareStatement.setString(1, userID)
                    prepareStatement.setString(2, password)
                    prepareStatement.setString(3, username)
                    prepareStatement.executeUpdate()
                }
            }
            true
        }
        catch (e: SQLException) {
            if (e.message?.contains("UNIQUE constraint failed") == true) {
                println("아이디가 이미 존재합니다.: $userID")
            }
            else {
                println("Error inserting user.: ${e.message}")
            }
            false
        }
    }

    fun getUser(userID: String, password: String): Boolean {
        val sql = "SELECT * FROM users WHERE userId = ? AND password = ?;"
        return try {
            DriverManager.getConnection(url).use { conn ->
                conn.prepareStatement(sql).use { prepareStatement ->
                    prepareStatement.setString(1, userID)
                    prepareStatement.setString(2, password)
                    prepareStatement.executeQuery().use { rs ->
                        rs.next()
                    }
                }
            }
        }
        catch (e: SQLException) {
            println("Error retrieving user: ${e.message}")
            false
        }
    }

    fun checkUserIDExists(userID: String): Boolean {
        val sql = "SELECT * FROM users WHERE userID = ?"
        return try {
            DriverManager.getConnection(url).use { conn ->
                conn.prepareStatement(sql).use { prepareStatement ->
                    prepareStatement.setString(1, userID)
                    prepareStatement.executeQuery().use { rs ->
                        rs.next()
                    }
                }
            }
        }
        catch (e: SQLException) {
            println("Error checking username: ${e.message}")
            false
        }
    }

    fun getUsernameByUserID(userID: String): String {
        val sql = "SELECT username FROM users WHERE userID = ?"
        return try {
            DriverManager.getConnection(url).use { conn ->
                conn.prepareStatement(sql).use { prepareStatement ->
                    prepareStatement.setString(1, userID)
                    prepareStatement.executeQuery().use { rs ->
                        if (rs.next()) {
                            rs.getString("username")
                        }
                        else ""
                    }
                }
            }
        }
        catch (e: SQLException) {
            println("Error retrieving username: ${e.message}")
            ""
        }
    }

    fun withdrawUser(userID: String, password: String): Boolean {
        val sql = "DELETE FROM users WHERE userID = ? AND password = ?;"
        return try {
            DriverManager.getConnection(url).use { conn ->
                conn.prepareStatement(sql).use { preparedStatement ->
                    preparedStatement.setString(1, userID)
                    preparedStatement.setString(2, password)
                    preparedStatement.executeUpdate() > 0
                }
            }
        }
        catch (e: SQLException) {
            println("Error withdrawing user: ${e.message}")
            false
        }
    }
}
