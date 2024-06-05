package kr.mjc.junghoon.javacapstonedesign.login

class UserManager {
    private val dbHelper = DatabaseHelper()

    fun register(username: String, userID: String, password: String): Boolean {
        return dbHelper.insertUser(userID, password, username)
    }

    fun login(userId: String, password: String): Boolean {
        return dbHelper.getUser(userId, password)
    }

    fun checkUserIDExists(userID: String): Boolean {
        return dbHelper.checkUserIDExists(userID)
    }

    fun getUsernameByUserID(userID: String): String {
        return dbHelper.getUsernameByUserID(userID)
    }

    fun withdrawUser(userID: String, password: String): Boolean {
        return dbHelper.withdrawUser(userID, password)
    }
}