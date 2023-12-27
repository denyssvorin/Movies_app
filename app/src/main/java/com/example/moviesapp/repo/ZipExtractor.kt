package com.example.moviesapp.repo

import android.content.Context
import android.graphics.BitmapFactory
import org.zeroturnaround.zip.ZipUtil
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import javax.inject.Inject


class ZipExtractor @Inject constructor(private val context: Context) {


    fun extractFromZip(inputStream: InputStream, destinationPath: String): List<ByteArray> {
        val list = mutableListOf<ByteArray>()
        var zipStream: ZipInputStream? = null
        try {
            zipStream = ZipInputStream(inputStream)
            var ze: ZipEntry
            while (zipStream.nextEntry.also { ze = it } != null) {
                if (ze.isDirectory) {
                    println("Folder : " + ze.name)
                    continue  //no need to extract
                }
                println("Extracting file " + ze.name)

                //at this moment zipStream pointing to the beginning of current ZipEntry, e.g. archived file
                //saving file
                val imageName = File(ze.name).name
                val test = ZipUtil.unpackEntry(zipStream, ze.name)
                println(test)

                val bitmap = BitmapFactory.decodeByteArray(test, 0, test.size)
                list.add(test)
            }
        } catch (e: Exception) {
            System.err.println("Error processing zip file : " + e.message)
        } finally {
            if (zipStream != null) try {
                zipStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return list
    }
}