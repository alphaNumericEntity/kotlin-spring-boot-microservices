package com.vnapnic.auth.controllers

import com.vnapnic.auth.services.*
import com.vnapnic.common.exception.SequenceException
import com.vnapnic.common.db.Account
import com.vnapnic.common.db.User
import com.vnapnic.common.enums.Role
import com.vnapnic.common.models.ErrorCode
import com.vnapnic.common.models.Response
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/register")
class RegisterController {
    private val log = LoggerFactory.getLogger(RegisterController::class.java)

    @Autowired
    lateinit var sequenceGeneratorService: SequenceGeneratorService

    @Autowired
    lateinit var authService: AuthService

    @RequestMapping(value = ["/staff"], method = [RequestMethod.POST])
    fun registerStaffWithEmail(@RequestBody json: Map<String, String>): Response {
        try {
            val code: String? = json["code"]
            val phoneNumber: String? = json["phoneNumber"]
            val socialId: String? = json["socialId"]
            val email: String? = json["email"]
            val password: String? = json["password"]
            // Device
            val deviceName: String? = json["deviceName"]
            val deviceId: String? = json["deviceId"]
            val platform: String? = json["platform"]

            log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

            if (code != null || code != "") {
                if (code?.startsWith("S") == false)
                    return Response.failed(error = ErrorCode.CODE_NOT_CORRECT)
            }

            if (email == null || email == "") {
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)
            }

            if (phoneNumber == null || phoneNumber == "") {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
            }

            if (password == null || password == "") {
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
            }

            if (authService.existsByEmail(email)) {
                return Response.failed(error = ErrorCode.EMAIL_IS_EXISTS)
            }

            if (authService.existsByPhoneNumber(phoneNumber)) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
            }

            // create staff Id
            val staffId = sequenceIDToStaffId(code ?: "S${Calendar.getInstance().get(Calendar.YEAR)}")

            val accountDTO = authService.save(staffId = staffId,
                    phoneNumber = phoneNumber,
                    socialId = socialId,
                    email = email,
                    password = password,
                    role = Role.STAFF,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    platform = platform)

            return Response.success(data = accountDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    private fun sequenceIDToStaffId(code: String): String {
        val sequenceID = sequenceGeneratorService.nextSequenceId(Account.SEQUENCE_NAME)
                ?: throw SequenceException("can't create staffID")
        return when (sequenceID) {
            in 100..999 -> {
                "$code-0${sequenceID}"
            }
            in 10..99 -> {
                "$code-00${sequenceID}"
            }
            in 1..9 -> {
                "$code-000${sequenceID}"
            }
            else -> {
                "$code-$sequenceID"
            }
        }
    }

    @RequestMapping(value = ["/customer"], method = [RequestMethod.POST])
    fun registerCustomer(@RequestBody json: Map<String, String>): Response {
        try {
            val phoneNumber: String? = json["phoneNumber"]
            val socialId: String? = json["socialId"]
            val email: String? = json["email"]
            val password: String? = json["password"]
            // Device
            val deviceName: String? = json["deviceName"]
            val deviceId: String? = json["deviceId"]
            val platform: String? = json["platform"]

            log.info(String.format("request with %s %s %s %s", phoneNumber, socialId, email, password))

            if (phoneNumber == null || phoneNumber == "") {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)
            }

            if (password == null || password == "") {
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)
            }

            if (authService.existsByPhoneNumber(phoneNumber)) {
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)
            }

            val accountDTO = authService.save(
                    staffId = null,
                    phoneNumber = phoneNumber,
                    socialId = socialId,
                    email = email,
                    password = password,
                    role = Role.CUSTOMER,
                    deviceId = deviceId,
                    deviceName = deviceName,
                    platform = platform)

            return Response.success(data = accountDTO)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }
}