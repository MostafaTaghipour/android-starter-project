package ir.rainyday.android.starter.errors

import com.google.gson.Gson
import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.data.model.json.HttpErrorBody
import ir.rainyday.android.starter.helpers.inject
import ir.rainyday.android.starter.net.HttpStatusCode


class HttpException (throwable: Throwable ) : BaseException(throwable){
    var statusCode: HttpStatusCode? = null
    var errorBody: HttpErrorBody? = null

    override val localizedDescription: String
        get() = this.errorBody?.userMessage?:context.getString(R.string.error_http_regular)


    init {
        @Suppress("DEPRECATION")
        if (throwable is retrofit2.adapter.rxjava2.HttpException){
            try {
                statusCode = HttpStatusCode.from(throwable.code())
                val errorJson = throwable.response().errorBody()!!.string()
                errorBody = inject<Gson>().fromJson(errorJson, HttpErrorBody::class.java)
            } catch (ex: Exception) {
            }
        }
    }
}

