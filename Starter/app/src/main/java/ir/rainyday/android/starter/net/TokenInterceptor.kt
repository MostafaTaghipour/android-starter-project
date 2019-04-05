package ir.rainyday.android.starter.net

import ir.rainyday.android.starter.helpers.inject
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by taghipour on 18/10/2017.
 */
class TokenInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val origin = chain.request()


        if (origin.url().url().path.toLowerCase().contains("logout"))
            return chain.proceed(origin)

        val request = origin.newBuilder()
                .header("Authorization", "bearer " + inject<CredentialsKeysRepo>().accessToken)
                .method(origin.method(), origin.body())
                .build()


        return chain.proceed(request)

    }
}