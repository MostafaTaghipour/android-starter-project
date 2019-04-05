package ir.rainyday.android.starter.net


import java.io.File
import java.io.IOException

import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.Util
import okio.BufferedSink
import okio.Okio

/**
 * Created by taghipour on 2/27/2017.
 */

class ProgressRequestBody(private val mFile: File, private val mContentType: String, private val tag: Int = 0, private val mListener: ProgressListener) : RequestBody() {

    override fun contentLength(): Long {
        return mFile.length()
    }

    override fun contentType(): MediaType? {
        return MediaType.parse(mContentType)
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val source = Okio.source(mFile)
        this.mListener.onStart(tag)
        try {
            val total = mFile.length()
            var uploaded: Long = 0
            var read: Long



            do {
                read = source.read(sink.buffer(), SEGMENT_SIZE.toLong())
                if (read == (-1).toLong())
                    break

                uploaded += read
                sink.flush()
                this.mListener.onProgress(
                        tag,
                        (100 * uploaded / total).toInt()
                )
            } while (true)

            this.mListener.onFinish(tag)
        } catch (throwable: Throwable) {
            this.mListener.onFailed(tag, throwable)
        } finally {
            Util.closeQuietly(source)
        }
    }

    interface ProgressListener {
        fun onStart(tag: Int) {}
        fun onProgress(tag: Int, percentage: Int)
        fun onFailed(tag: Int, error: Throwable) {}
        fun onFinish(tag: Int) {}
    }

    companion object {

        private val SEGMENT_SIZE = 2048 // okio.Segment.SIZE
    }
}