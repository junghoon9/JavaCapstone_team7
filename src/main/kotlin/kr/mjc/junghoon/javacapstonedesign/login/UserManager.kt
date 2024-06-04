package kr.mjc.junghoon.javacapstonedesign.login

class UserManager {
    private val users = mutableListOf<User>()

    fun register(username: String, userId: String, password: String): Boolean {
        if (users.any { it.userId == userId }) {
            return false // 이미 존재하는 사용자
        }
        users.add(User(username, userId, password))
        return true
    }

    fun login(userId: String, password: String): Boolean {
        return users.any { it.userId == userId && it.password == password }
    }
}