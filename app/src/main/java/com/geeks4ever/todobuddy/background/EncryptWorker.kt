package com.geeks4ever.todobuddy.background

import android.content.ContentValues.TAG
import android.content.Context
import android.media.MediaCodec.CryptoException
import android.os.Environment
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.security.InvalidKeyException
import java.security.Key
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.NoSuchPaddingException
import javax.crypto.spec.SecretKeySpec


class EncryptWorker(val context: Context, workerParameters: WorkerParameters) : CoroutineWorker(context, workerParameters)  {

    override suspend fun doWork(): Result {

        return encrypt()

    }

    @Throws(
        IOException::class,
        NoSuchAlgorithmException::class,
        NoSuchPaddingException::class,
        InvalidKeyException::class
    )

     fun encrypt(): Result {

        val ipFileName = "Log_${context.getSharedPreferences("log", 0)
            .getInt("currentLogName", 1)}.txt"

        val opFileName = "Log_${context.getSharedPreferences("log", 0)
            .getInt("currentLogName", 1)}.enc"

        val dir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
        val ipFileInDir = File(dir, ipFileName)
        val opFileInDir = File(dir, opFileName)
        val fis = FileInputStream(ipFileInDir)
        val fos = FileOutputStream(opFileInDir, true)

        doCryptoInAES(Cipher.ENCRYPT_MODE, "ASDFGHJKLASDFGHJ", ipFileInDir, opFileInDir)


        Log.d(TAG, "encrypted")

        return Result.success()
    }


    @Throws(CryptoException::class)
    private fun doCryptoInAES(
        cipherMode: Int, key: String, inputFile: File,
        outputFile: File
    ) {
        try {
            val secretKey: Key = SecretKeySpec(key.toByteArray(), "AES")
            val cipher = Cipher.getInstance("AES")
            cipher.init(cipherMode, secretKey)
            val inputStream = FileInputStream(inputFile)
            val inputBytes = ByteArray(inputFile.length().toInt())
            inputStream.read(inputBytes)
            val outputBytes = cipher.doFinal(inputBytes)
            val outputStream = FileOutputStream(outputFile)
            outputStream.write(outputBytes)
            inputStream.close()
            outputStream.close()
        } catch (ex: Exception) {
            Log.e(TAG, "doCryptoInAES: ", ex )
        }
    }

}