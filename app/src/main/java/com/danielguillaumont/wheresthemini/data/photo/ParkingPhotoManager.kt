package com.danielguillaumont.wheresthemini.data.photo

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

data class PendingParkingPhoto(
    val filePath: String,
    val uri: Uri
)

class ParkingPhotoManager(
    private val context: Context
) {

    fun createParkingPhoto():
            PendingParkingPhoto {

        val picturesDirectory =
            context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ) ?: context.filesDir

        if (
            !picturesDirectory.exists()
        ) {
            picturesDirectory.mkdirs()
        }

        val photoFile =
            File.createTempFile(
                "mini_${System.currentTimeMillis()}_",
                ".jpg",
                picturesDirectory
            )

        val photoUri =
            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                photoFile
            )

        return PendingParkingPhoto(
            filePath =
                photoFile.absolutePath,

            uri =
                photoUri
        )
    }

    fun deletePhoto(
        filePath: String
    ) {
        runCatching {
            File(
                filePath
            ).delete()
        }
    }
}