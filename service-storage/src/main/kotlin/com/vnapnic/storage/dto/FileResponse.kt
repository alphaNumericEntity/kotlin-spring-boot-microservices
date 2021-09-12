package com.vnapnic.storage.dto

class FileResponse(
        val fileId: String? = null,
        val fileName: String? = null, //Upload file name
        val fileExtName: String? = null, //Suffix name
        val contentType: String? = null //Upload file type
)