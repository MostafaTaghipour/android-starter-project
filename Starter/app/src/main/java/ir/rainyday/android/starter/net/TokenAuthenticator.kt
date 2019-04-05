package ir.rainyday.android.starter.net

import ir.rainyday.android.starter.helpers.inject
import ir.rainyday.android.starter.modules.shared.Navigator
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

/**
 * Created by taghipour on 18/10/2017.
 */
class TokenAuthenticator : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if (tryCount > MAX_TRY) {
            Navigator.logout()
        } else {
            tryCount++
            return response.request().newBuilder()
                    .header("Authorization", "Bearer " + inject<CredentialsKeysRepo>().accessToken)
                    .build()
        }


        return null

    }

    companion object {
        private const val MAX_TRY = 6
        private var tryCount = 0
    }
}