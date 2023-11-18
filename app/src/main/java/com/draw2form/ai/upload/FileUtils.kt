package com.draw2form.ai.upload

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


/**
 * Copied from https://gist.github.com/walkingError/915c73ae48882072dc0e8467a813046f
 * Modified with https://github.com/Javernaut/WhatTheCodec/issues/2
 */
object FileUtils {
    /**
     * Get a file from a Uri.
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */
    @Throws(Exception::class)
    fun getFileFromUri(context: Context, uri: Uri): File {
        var path: String? = null

        if (DocumentsContract.isDocumentUri(context, uri)) { // TODO: 2015. 11. 17. KITKAT
            println("isDocumentUri")
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    path = Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) { // DownloadsProvider
                println("isDownloadsDocument")
                println(Build.VERSION.SDK_INT)

                val id = DocumentsContract.getDocumentId(uri)
                println(id)

                if (id != null && id.startsWith("msf:")) {
                    val file = File(context.cacheDir, uri.lastPathSegment)
                    try {
                        context.contentResolver.openInputStream(uri).use { inputStream ->
                            FileOutputStream(file).use { output ->
                                // You may need to change buffer size. I use large buffer size to help loading large file , but be ware of
                                //  OutOfMemory Exception
                                val buffer = ByteArray(8 * 1024)
                                var read: Int
                                while (inputStream!!.read(buffer).also { read = it } != -1) {
                                    output.write(buffer, 0, read)
                                }
                                output.flush()
                                path = file.path
                                println("Path Savede: $path")
                            }
                        }
                    } catch (ex: IOException) {
                        println(ex)
                        ex.printStackTrace()
                    }
                } else {

                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
                    )
                    path = getDataColumn(context, contentUri, null, null)
                }

            } else if (isMediaDocument(uri)) { // MediaProvider
                println("isMediaDocument")

                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                path = getDataColumn(context, contentUri, selection, selectionArgs)
            } else if (isGoogleDrive(uri)) { // Google Drive
                println("isGoogleDrive")

                val TAG = "isGoogleDrive"
                path = TAG
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val acc = split[0]
                val doc = split[1]

                /*
                     * @details google drive document data. - acc , docId.
                     * */return saveFileIntoExternalStorageByUri(context, uri)
            } // MediaStore (and general)
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            println("\"content\".equals(uri.scheme")
            path = getDataColumn(context, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            println("\"file\".equals(uri.scheme")
            path = uri.path
        }
        return File(path)
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is GoogleDrive.
     */
    private fun isGoogleDrive(uri: Uri): Boolean {
        return uri.authority.equals("com.google.android.apps.docs.storage", ignoreCase = true)
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String>?
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    private fun makeEmptyFileIntoExternalStorageWithTitle(title: String?): File {
        val root = Environment.getExternalStorageDirectory().absolutePath
        return File(root, title)
    }

    private fun getFileName(context: Context, uri: Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
//                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    @Throws(Exception::class)
    fun saveBitmapFileIntoExternalStorageWithTitle(bitmap: Bitmap, title: String) {
        val fileOutputStream = FileOutputStream(makeEmptyFileIntoExternalStorageWithTitle("$title.png"))
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
        fileOutputStream.close()
    }

    @Throws(Exception::class)
    fun saveFileIntoExternalStorageByUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalSize = inputStream!!.available()
        var bis: BufferedInputStream? = null
        var bos: BufferedOutputStream? = null
        val fileName = getFileName(context, uri)
        val file = makeEmptyFileIntoExternalStorageWithTitle(fileName)
        bis = BufferedInputStream(inputStream)
        bos = BufferedOutputStream(
            FileOutputStream(
                file, false
            )
        )
        val buf = ByteArray(originalSize)
        bis.read(buf)
        do {
            bos.write(buf)
        } while (bis.read(buf) != -1)
        bos.flush()
        bos.close()
        bis.close()
        return file
    }
}