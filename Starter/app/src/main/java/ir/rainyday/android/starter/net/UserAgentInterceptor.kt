package ir.rainyday.android.starter.net

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Created by taghipour on 18/10/2017.
 */
class UserAgentInterceptor(private val userAgentHeaderValue: String) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithUserAgent = originalRequest.newBuilder()
                .removeHeader(USER_AGENT_HEADER_NAME)
                .addHeader(USER_AGENT_HEADER_NAME, userAgentHeaderValue)
                .build()
        return chain.proceed(requestWithUserAgent)
    }

    companion object {
        private val USER_AGENT_HEADER_NAME = "UserInfo-Agent"
    }
}