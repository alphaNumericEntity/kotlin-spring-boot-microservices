package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.vnapnic.database.enums.Platform
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "device")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class DeviceBean(
        @JsonProperty("deviceId")
        @Id
        var deviceId: String? = null,
        @JsonProperty("deviceName")
        var deviceName: String? = null,
        @JsonProperty("platform")
        var platform: Platform = Platform.OTHER
) {
    override fun toString(): String {
        return "Device(deviceId=$deviceId, deviceName=$deviceName, platform=$platform)"
    }
}