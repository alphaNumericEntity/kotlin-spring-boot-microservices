package com.vnapnic.auth.controllers

import com.vnapnic.auth.domain.RegisterRequest
import com.vnapnic.auth.services.*
import com.vnapnic.common.exception.SequenceException
import com.vnapnic.database.enums.Role
import com.vnapnic.common.beans.ErrorCode
import com.vnapnic.common.beans.Response
import com.vnapnic.common.utils.isEmail
import com.vnapnic.common.utils.isPhoneNumber
import com.vnapnic.database.beans.AccountBean
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

    /**
     * Staff can use account with the phone number or email
     */
    @RequestMapping(value = ["collaborator"], method = [RequestMethod.POST])
    fun collaboratorRegister(@RequestBody request: RegisterRequest?): Response {
        try {

            if (request == null)
                return Response.failed(error = ErrorCode.WARNING_DATA_FORMAT)

            if (request.code.isNullOrEmpty())
                return Response.failed(error = ErrorCode.CODE_NOT_CORRECT)

            if (request.email.isNullOrEmpty())
                return Response.failed(error = ErrorCode.EMAIL_IS_NULL_BLANK)

            if (!request.email.isEmail())
                return Response.failed(error = ErrorCode.EMAIL_WRONG_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)

            if (!request.phoneNumber.isPhoneNumber())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)

            if (authService.existsByEmail(request.email))
                return Response.failed(error = ErrorCode.EMAIL_IS_EXISTS)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)

            // create staff Id
            val staffId = sequenceIDToStaffId( "${request.code}${Calendar.getInstance().get(Calendar.YEAR)}")

            val dto = authService.saveAccount(staffId = staffId,
                    phoneNumber = request.phoneNumber,
                    socialId = request.socialId,
                    email = request.email,
                    password = request.password,
                    role = Role.STAFF,
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform)

            return Response.success(data = dto)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }

    private fun sequenceIDToStaffId(code: String): String {
        val sequenceID = sequenceGeneratorService.nextSequenceId(AccountBean.SEQUENCE_NAME)
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

    /**
     * Customer only account is phone number
     */
    @RequestMapping(value = ["/customer"], method = [RequestMethod.POST])
    fun customerRegister(@RequestBody request: RegisterRequest?): Response {
        try {
            if (request == null)
                return Response.failed(error = ErrorCode.WARNING_DATA_FORMAT)

            if (request.phoneNumber.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_NULL_BLANK)

            if (!request.phoneNumber.isPhoneNumber())
                return Response.failed(error = ErrorCode.PHONE_NUMBER_WRONG_FORMAT)

            if (request.password.isNullOrEmpty())
                return Response.failed(error = ErrorCode.PASSWORD_IS_NULL_BLANK)

            if (authService.existsByPhoneNumber(request.phoneNumber))
                return Response.failed(error = ErrorCode.PHONE_NUMBER_IS_EXISTS)

            val dto = authService.saveAccount(
                    staffId = null,
                    phoneNumber = request.phoneNumber,
                    socialId = request.socialId,
                    email = request.email,
                    password = request.password,
                    role = Role.CUSTOMER,
                    deviceId = request.deviceId,
                    deviceName = request.deviceName,
                    platform = request.platform)

            return Response.success(data = dto)
        } catch (e: Exception) {
            e.printStackTrace()
            return Response.failed(error = ErrorCode.SERVER_UNKNOWN_ERROR)
        }
    }
}