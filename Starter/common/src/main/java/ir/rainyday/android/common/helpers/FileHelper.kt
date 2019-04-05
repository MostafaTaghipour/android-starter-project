package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 17/09/2017.
 */


import java.io.BufferedReader
import java.io.Closeable
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.util.ArrayList

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import android.text.TextUtils


/**
 * File Utils
 *
 * Read or write file
 *  * [.readFile] read file
 *  * [.readFileToList] read file to string list
 *  * [.writeFile] write file from String
 *  * [.writeFile] write file from String
 *  * [.writeFile] write file from String List
 *  * [.writeFile] write file from String List
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *  * [.writeFile] write file
 *
 *
 * Operate file
 *  * [.moveFile] or [.moveFile]
 *  * [.copyFile]
 *  * [.getFileExtension]
 *  * [.getFileName]
 *  * [.getFileNameWithoutExtension]
 *  * [.getFileSize]
 *  * [.deleteFile]
 *  * [.isFileExist]
 *  * [.isFolderExist]
 *  * [.makeFolders]
 *  * [.makeDirs]
 *
 *
 * @author [Trinea](http://www.trinea.cn) 2012-5-12
 */
object FileHelper {

    val FILE_EXTENSION_SEPARATOR = "."
    val FILE_SEPARATOR = File.separator

    //region privates
    private val context: Context?
        get() = GlobalAppContext.instance.applicationContext

    /**
     * Close closable object and wrap [IOException] with [RuntimeException]
     *
     * @param closeable closeable object
     */
    private fun close(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                throw RuntimeException("IOException occurred. ", e)
            }

        }
    }

    /**
     * Close closable and hide possible [IOException]
     *
     * @param closeable closeable object
     */
    private fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (e: IOException) {
                // Ignored
            }

        }
    }


    /**
     * is null or its size is 0
     *
     *
     * <pre>
     * isListEmpty(null)   =   true;
     * isListEmpty({})     =   true;
     * isListEmpty({1})    =   false;
    </pre> *
     *
     * @param <V>
     * @param sourceList
     * @return if list is null or its size is 0, return true, else return false.
    </V> */
    private fun <V> isListEmpty(sourceList: List<V>?): Boolean {
        return sourceList == null || sourceList.size == 0
    }


    /**
     * is null or its length is 0 or it is made by space
     *
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
    </pre> *
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    private fun isStringBlank(str: String?): Boolean {
        return str == null || str.trim { it <= ' ' }.length == 0
    }
    //endregion

    //region Directories
    enum class Directory(internal val dir: String) {
        DCIM(Environment.DIRECTORY_DCIM),
        DOWNLOADS(Environment.DIRECTORY_DOWNLOADS),
        PICTURES(Environment.DIRECTORY_PICTURES),
        MOVIES(Environment.DIRECTORY_MOVIES),
        MUSIC(Environment.DIRECTORY_MUSIC)
    }


    val isExternalStorageWritable
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    val isExternalStorageReadable
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED_READ_ONLY || isExternalStorageWritable


    /**
     * getInstance external storage private files directory
     * in "/Android/data/[packagename]/files"
     * this files will be deleteLocal after uninstall app
     * this files are accessible to another apps
     *
     * @return
     */
    val externalFilesDir: File?
        get() = context?.getExternalFilesDir(null)

    val externalFilesDirPath: String
        get() = externalFilesDir!!.toString()

    /**
     * getInstance external storage private cache directory
     * in "/Android/data/[packagename]/cache"
     * this files will be deleteLocal after uninstall app
     * this files are accessible to another apps
     *
     * @return
     */
    val externalCacheDir: File?
        get() = context?.externalCacheDir

    val externalCacheDirPath: String?
        get() = externalCacheDir?.toString()

    /**
     * getInstance internal storage private files directory
     * in "/data/data/[packagename]/files"
     * this files will be deleteLocal after uninstall app
     * this files are not accessible to another apps
     *
     * @return
     */
    val internalFilesDir: File?
        get() = context?.filesDir

    val internalFilesDirPath: String?
        get() = internalFilesDir?.toString()

    /**
     * getInstance internal storage private cache directory
     * in "/data/data/[packagename]/cache"
     * this files will be deleteLocal after uninstall app
     * this files are not accessible to another apps
     *
     * @return
     */
    val internalCacheDir: File?
        get() = context?.cacheDir

    val internalCacheDirPath: String?
        get() = internalCacheDir?.toString()

    val externalStorageDirectoryPath: String?
        get() = externalStorageDirectory.toString()

    /**
     * getInstance external storage public directory (root)
     * this files will not be deleteLocal after uninstall app
     * this files are accessible to another apps
     *
     * @return
     */
    val externalStorageDirectory: File
        get() = Environment.getExternalStorageDirectory()


    /**
     * getInstance external storage public directory file type based  (Pictures, Music, ...)
     * this files will not be deleteLocal after uninstall app
     * this files are accessible to another apps
     *
     * @return
     */
    fun getExternalStorageDirectory(directory: Directory): File {
        return Environment.getExternalStoragePublicDirectory(directory.dir)
    }

    fun getExternalStorageDirectoryPath(directory: Directory): String {
        return getExternalStorageDirectory(directory).toString()
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br></br>
     * <br></br>
     *
     * **Attentions:**
     *  * makeDirs("C:\\Users\\Trinea") can only create users folder
     *  * makeFolder("C:\\Users\\Trinea\\") can create Trinea folder
     *
     *
     * @param fullPath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     *
     *  * if [FileHelper.getFolderName] return null, return false
     *  * if target directory already exists, return true
     *
     */
    fun makeDirs(fullPath: String): Boolean {
        val folderName = getDirName(fullPath)
        if (folderName.isNullOrEmpty()) {
            return false
        }

        val folder = File(folderName)
        return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
    }

    fun makeDirs(path: String, name: String): Boolean {
        return if (path.isNullOrEmpty() || name.isNullOrEmpty()) {
            false
        } else makeDirs(path + FILE_SEPARATOR + name + FILE_SEPARATOR)

    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    fun isDirExist(directoryPath: String): Boolean {
        if (isStringBlank(directoryPath)) {
            return false
        }

        val dire = File(directoryPath)
        return dire.exists() && dire.isDirectory
    }

    /**
     * getInstance folder name from path
     *
     *
     * <pre>
     * getFolderName(null)               =   null
     * getFolderName("")                 =   ""
     * getFolderName("   ")              =   ""
     * getFolderName("a.mp3")            =   ""
     * getFolderName("a.b.rmvb")         =   ""
     * getFolderName("abc")              =   ""
     * getFolderName("c:\\")              =   "c:"
     * getFolderName("c:\\a")             =   "c:"
     * getFolderName("c:\\a.b")           =   "c:"
     * getFolderName("c:a.txt\\a")        =   "c:a.txt"
     * getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     * getFolderName("/home/admin")      =   "/home"
     * getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
    </pre> *
     *
     * @param filePath
     * @return
     */
    fun getDirName(filePath: String): String {

        if (filePath.isNullOrEmpty()) {
            return filePath
        }

        val filePosi = filePath.lastIndexOf(FILE_SEPARATOR)
        return if (filePosi == -1) "" else filePath.substring(0, filePosi)
    }


    /**
     * deleteLocal file or directory
     *
     *  * if path is null or empty, return true
     *  * if path not exist, return true
     *  * if path exist, deleteLocal recursion. return true
     *
     *
     * @param path
     * @return
     */
    fun deleteDir(path: String): Boolean {
        return deleteFile(path)
    }
    //endregion

    //region Files
    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported [&lt;/code&gt;charset&lt;code&gt;][java.nio.charset.Charset]
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    fun readFile(filePath: String, charsetName: String): StringBuilder? {
        val file = File(filePath)
        val fileContent = StringBuilder("")
        if (!file.isFile) {
            return null
        }

        var reader: BufferedReader? = null
        try {
            val `is` = InputStreamReader(FileInputStream(file), charsetName)
            reader = BufferedReader(`is`)
            var line: String?
            do {
                line = reader.readLine()
                if (line == null)
                    break

                if (fileContent.toString() != "") {
                    fileContent.append("\r\n")
                }
                fileContent.append(line)
            } while (true)

            return fileContent
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(reader)
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if content is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    @JvmOverloads
    fun writeFile(filePath: String, content: String, append: Boolean = false): Boolean {
        if (content.isEmpty()) {
            return false
        }

        var fileWriter: FileWriter? = null
        try {
            makeDirs(filePath)
            fileWriter = FileWriter(filePath, append)
            fileWriter.write(content)
            return true
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(fileWriter)
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param contentList
     * @param append      is append, if true, write to the end of file, else clear content of file and write into it
     * @return return false if contentList is empty, true otherwise
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    @JvmOverloads
    fun writeFile(filePath: String, contentList: List<String>, append: Boolean = false): Boolean {
        if (isListEmpty(contentList)) {
            return false
        }

        var fileWriter: FileWriter? = null
        try {
            makeDirs(filePath)
            fileWriter = FileWriter(filePath, append)
            var i = 0
            for (line in contentList) {
                if (i++ > 0) {
                    fileWriter.write("\r\n")
                }
                fileWriter.write(line)
            }
            return true
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(fileWriter)
        }
    }

    /**
     * write file
     *
     * @param stream the input stream
     * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    @JvmOverloads
    fun writeFile(filePath: String?, stream: InputStream, append: Boolean = false): Boolean {
        return writeFile(if (filePath != null) File(filePath) else null, stream, append)
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if `true`, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    @JvmOverloads
    fun writeFile(file: File?, stream: InputStream, append: Boolean = false): Boolean {
        var o: OutputStream? = null
        try {
            makeDirs(file!!.absolutePath)
            o = FileOutputStream(file, append)
            val data = ByteArray(1024)
            var length: Int
            do {
                length = stream.read(data)
                if (length == -1)
                    break
                o.write(data, 0, length)
            } while (true)

            o.flush()
            return true
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(o)
            close(stream)
        }
    }

    /**
     * move file
     *
     * @param sourceFilePath
     * @param destFilePath
     */
    fun moveFile(sourceFilePath: String, destFilePath: String) {
        if (TextUtils.isEmpty(sourceFilePath) || TextUtils.isEmpty(destFilePath)) {
            throw RuntimeException("Both sourceFilePath and destFilePath cannot be null.")
        }
        moveFile(File(sourceFilePath), File(destFilePath))
    }

    /**
     * move file
     *
     * @param srcFile
     * @param destFile
     */
    fun moveFile(srcFile: File, destFile: File) {
        val rename = srcFile.renameTo(destFile)
        if (!rename) {
            copyFile(srcFile.absolutePath, destFile.absolutePath)
            deleteFile(srcFile.absolutePath)
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    fun copyFile(sourceFilePath: String, destFilePath: String): Boolean {
        val inputStream: InputStream?
        try {
            inputStream = FileInputStream(sourceFilePath)
        } catch (e: FileNotFoundException) {
            throw RuntimeException("FileNotFoundException occurred. ", e)
        }

        return writeFile(destFilePath, inputStream)
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported [&lt;/code&gt;charset&lt;code&gt;][java.nio.charset.Charset]
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    fun readFileToList(filePath: String, charsetName: String): List<String>? {
        val file = File(filePath)
        val fileContent = ArrayList<String>()
        if (!file.isFile) {
            return null
        }

        var reader: BufferedReader? = null
        try {
            val `is` = InputStreamReader(FileInputStream(file), charsetName)
            reader = BufferedReader(`is`)
            var line: String?
            do {
                line = reader.readLine()
                if (line == null)
                    break

                fileContent.add(line)
            } while (true)

            return fileContent
        } catch (e: IOException) {
            throw RuntimeException("IOException occurred. ", e)
        } finally {
            close(reader)
        }
    }

    /**
     * getInstance file name from path, not include suffix
     *
     *
     * <pre>
     * getFileNameWithoutExtension(null)               =   null
     * getFileNameWithoutExtension("")                 =   ""
     * getFileNameWithoutExtension("   ")              =   "   "
     * getFileNameWithoutExtension("abc")              =   "abc"
     * getFileNameWithoutExtension("a.mp3")            =   "a"
     * getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     * getFileNameWithoutExtension("c:\\")              =   ""
     * getFileNameWithoutExtension("c:\\a")             =   "a"
     * getFileNameWithoutExtension("c:\\a.b")           =   "a"
     * getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     * getFileNameWithoutExtension("/home/admin")      =   "admin"
     * getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
    </pre> *
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    fun getFileNameWithoutExtension(filePath: String): String {
        if (filePath.isNullOrEmpty()) {
            return filePath
        }

        val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val filePosi = filePath.lastIndexOf(FILE_SEPARATOR)
        if (filePosi == -1) {
            return if (extenPosi == -1) filePath else filePath.substring(0, extenPosi)
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1)
        }
        return if (filePosi < extenPosi) filePath.substring(filePosi + 1, extenPosi) else filePath.substring(filePosi + 1)
    }

    /**
     * getInstance file name from path, include suffix
     *
     *
     * <pre>
     * getFileName(null)               =   null
     * getFileName("")                 =   ""
     * getFileName("   ")              =   "   "
     * getFileName("a.mp3")            =   "a.mp3"
     * getFileName("a.b.rmvb")         =   "a.b.rmvb"
     * getFileName("abc")              =   "abc"
     * getFileName("c:\\")              =   ""
     * getFileName("c:\\a")             =   "a"
     * getFileName("c:\\a.b")           =   "a.b"
     * getFileName("c:a.txt\\a")        =   "a"
     * getFileName("/home/admin")      =   "admin"
     * getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
    </pre> *
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    fun getFileName(filePath: String): String {
        if (filePath.isNullOrEmpty()) {
            return filePath
        }

        val filePosi = filePath.lastIndexOf(FILE_SEPARATOR)
        return if (filePosi == -1) filePath else filePath.substring(filePosi + 1)
    }

    /**
     * getInstance suffix of file from path
     *
     *
     * <pre>
     * getFileExtension(null)               =   ""
     * getFileExtension("")                 =   ""
     * getFileExtension("   ")              =   "   "
     * getFileExtension("a.mp3")            =   "mp3"
     * getFileExtension("a.b.rmvb")         =   "rmvb"
     * getFileExtension("abc")              =   ""
     * getFileExtension("c:\\")              =   ""
     * getFileExtension("c:\\a")             =   ""
     * getFileExtension("c:\\a.b")           =   "b"
     * getFileExtension("c:a.txt\\a")        =   ""
     * getFileExtension("/home/admin")      =   ""
     * getFileExtension("/home/admin/a.txt/b")  =   ""
     * getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
    </pre> *
     *
     * @param filePath
     * @return
     */
    fun getFileExtension(filePath: String): String? {
        if (isStringBlank(filePath)) {
            return filePath
        }

        val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val filePosi = filePath.lastIndexOf(FILE_SEPARATOR)
        if (extenPosi == -1) {
            return ""
        }
        return if (filePosi >= extenPosi) "" else filePath.substring(extenPosi + 1)
    }

    /**
     * make New File
     *
     * @param path
     * @param name
     * @return
     */
    fun makeNewFile(path: String, name: String): Boolean {
        return if (path.isNullOrEmpty() || name.isNullOrEmpty()) {
            false
        } else makeNewFile(path + FILE_SEPARATOR + name)

    }

    fun makeNewFile(fullPath: String): Boolean {
        if (fullPath.isNullOrEmpty()) {
            return false
        }

        val file = File(fullPath)

        if (file.exists()) {
            return true
        }

        try {
            file.createNewFile()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return false
    }


    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    fun isFileExist(filePath: String): Boolean {
        if (isStringBlank(filePath)) {
            return false
        }

        val file = File(filePath)
        return file.exists() && file.isFile
    }


    /**
     * deleteLocal file or directory
     *
     *  * if path is null or empty, return true
     *  * if path not exist, return true
     *  * if path exist, deleteLocal recursion. return true
     *
     *
     * @param path
     * @return
     */
    fun deleteFile(path: String): Boolean {
        if (isStringBlank(path)) {
            return true
        }

        val file = File(path)
        if (!file.exists()) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return file.delete()
    }


    /**
     * getInstance file size
     *
     *  * if path is null or empty, return -1
     *  * if path exist and it is a file, return file size, else return -1
     *
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    fun getFileSize(path: String): Long {
        if (isStringBlank(path)) {
            return -1
        }

        val file = File(path)
        return if (file.exists() && file.isFile) file.length() else -1
    }
    //endregion

    //region Path
    /**
     * Get Path from  URI
     *
     * @param uri
     * @return
     */
    fun getPath(uri: Uri): String? {
        try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = this.context?.contentResolver?.query(uri, projection, null, null, null)
            val column_index: Int
            return if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                cursor.moveToFirst()
                val path = cursor.getString(column_index)
                cursor.close()
                path
            } else
                uri.path
        } catch (e: Exception) {
            return null
        }
    }


    /**
     * getInstance Uri from path
     *
     * from api 24 you have to use file provider
     * https://stackoverflow.com/questions/38200282/android-os-fileuriexposedexception-file-storage-emulated-0-test-txt-exposed/38858040#38858040
     *
     * @param path
     * @return
     */
    @JvmOverloads
    fun getUri(path: String,authority:String? = null): Uri? {

        fromApi(24) {
            val auth = authority ?: CurrentApp.Id + ".FileProvider"
            context?.let {
                return FileProvider.getUriForFile(it, auth, File(path))
            }
        }

        return Uri.fromFile(File(path))
    }
    //endregion

}

