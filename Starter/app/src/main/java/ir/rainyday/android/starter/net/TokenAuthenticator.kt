package ir.rainyday.android.starter.net

import ir.rainyday.android.starter.data.model.json.RefreshTokenResponseBody
import ir.rainyday.android.starter.helpers.inject
import ir.rainyday.android.starter.modules.shared.Navigator
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.io.IOException

/**
 * Created by taghipour on 18/10/2017.
 */

private const val MAX_TRY = 3
private var tryCount = 0

class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val tokenManager: CredentialsKeysRepo = inject()

        val accessToken = tokenManager.accessToken
        val refreshToken = tokenManager.refreshToken

        synchronized(this) {
            val originRequest = response.request()

            // Validate that the tokens were not mutated by another thread
            if (accessToken != tokenManager.accessToken || refreshToken != tokenManager.refreshToken) {

                // Tokens have been mutated, retry request with updated auth
                if (!accessToken.isEmpty()) return originRequest.withAuthorization(accessToken)
            }

            // Request fresh access token
            try {


                //TODO renew token
                val renewResponse: retrofit2.Response<RefreshTokenResponseBody> = retrofit2.Response.success(RefreshTokenResponseBody(true, "", ""))
                val body = renewResponse.body()

                if (renewResponse.isSuccessful && body != null) {
                    val newToken = body.accessToken
                    val newRefreshToken = body.refreshToken
                    val usedToken = response.request().getAuthorizationToken()

                    if (newToken != usedToken) {
                        tokenManager.accessToken = newToken
                        tokenManager.refreshToken = newRefreshToken
                        tokenManager.sync()

                        return originRequest.withAuthorization(newToken)
                    } else {
                    }
                } else if (renewResponse.code() == HttpStatusCode.UNAUTHORIZED.value || renewResponse.code() == HttpStatusCode.BAD_REQUEST.value) {
                    unauthorized()
                } else {
                    checkRetryCount()
                }
            } catch (e: IOException) {
                checkRetryCount()
            } catch (e: Exception) {
                checkRetryCount()
            }
        }
        return null
    }

    private fun checkRetryCount() {
        if (tryCount > MAX_TRY) {
            unauthorized()
        } else {
            tryCount++
        }
    }

    private fun unauthorized() {
        Navigator.logout()
        tryCount = 0
    }
}

internal fun Request.withAuthorization(accessToken: String) =
        this.newBuilder()
                .header("Authorization", "Bearer $accessToken")
                .build()

internal fun Request.getAuthorizationToken(): String? =
        this.header("Authorization")
                ?.replace("Bearer", "")
                ?.trim()
