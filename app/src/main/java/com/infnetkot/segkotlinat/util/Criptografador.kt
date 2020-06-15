package com.infnetkot.segkotlinat.util

import android.app.Activity
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKeys
import java.io.File

class Criptografador {
    companion object{
        private var criptografador : Criptografador? = null

        fun getInstance() : Criptografador{
            if(criptografador == null)
                criptografador = Criptografador()

            return criptografador as Criptografador
        }
    }

    fun getEncFile(nome: String, activity: Activity): EncryptedFile {
        var path = activity.applicationContext.filesDir
        var dir = File(path, "/ARQSLOC")
        if (!dir.exists() || !dir.isDirectory) {
            val arqLoqDirectory = File(path, "ARQSLOC")
            arqLoqDirectory.mkdirs()
            dir = File(path, "/ARQSLOC")
        }

        val masterKeyAlias: String =
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        val file = File(dir, nome)
        return EncryptedFile.Builder(
            file,
            activity.applicationContext,
            masterKeyAlias,
            EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB)
            .build()
    }
}