package com.vnapnic.user.services

import com.vnapnic.database.entities.UserEntity
import com.vnapnic.database.enums.Gender
import com.vnapnic.database.exception.UserNotFound
import com.vnapnic.user.dto.UserResponse
import com.vnapnic.user.repositories.AccountRepository
import com.vnapnic.user.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.jvm.Throws

interface UserService {

    @Throws(Exception::class)
    fun getUserIdByAccountId(accountId: String): String?

    @Throws(Exception::class)
    fun findById(userId: String?): UserResponse

    @Throws(Exception::class)
    fun updateProfile(userId: String?, firstName: String?, lastName: String?, weight: Double?, height: Double?, gender: Gender, description: String?): UserResponse
}

@Service
class UserServiceImpl : UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Throws(Exception::class)
    override fun getUserIdByAccountId(accountId: String): String? {
        val account = accountRepository.findById(accountId).get()
        return account.info?.id
    }

    @Throws(Exception::class)
    override fun findById(userId: String?): UserResponse {
        if (userId == null || !userRepository.existsById(userId))
            throw UserNotFound("User not found")
        val result = userRepository.findById(userId).get()
        return UserResponse(
                userId = result.id,
                firstName = result.firstName,
                lastName = result.lastName,
                weight = result.weight,
                height = result.height,
                gender = result.gender,
                description = result.description,
                avatar = result.avatar
        )
    }

    @Throws(Exception::class)
    override fun updateProfile(userId: String?, firstName: String?, lastName: String?, weight: Double?, height: Double?, gender: Gender, description: String?): UserResponse {

        val dto = findById(userId)
        dto.firstName = firstName
        dto.lastName = lastName
        dto.weight = weight
        dto.height = height
        dto.gender = gender
        dto.description = description

        val user = UserEntity(
                id = userId,
                firstName = firstName,
                lastName = lastName,
                weight = weight,
                height = height,
                gender = gender,
                description = description,
                avatar = dto.avatar,
        )

        val result = userRepository.save(user)
        return UserResponse(
                userId = result.id,
                firstName = result.firstName,
                lastName = result.lastName,
                weight = result.weight ?: 0.0,
                height = result.height ?: 0.0,
                gender = result.gender,
                description = result.description,
                avatar = result.avatar
        )
    }
}