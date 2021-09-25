package com.vnapnic.database.beans

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "clean_tool")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CleanToolBean(@JsonProperty("id")
                     @Id
                     var id: String = "",
                         var name:String? =null,
                         var description: String? = null
                    )