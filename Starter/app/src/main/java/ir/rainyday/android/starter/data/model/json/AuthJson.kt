package ir.rainyday.android.starter.data.model.json

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequestBody(
        @SerializedName("refreshToken")
        val refreshToken: String
)

data class RefreshTokenResponseBody(
        @SerializedName("isAuthenticated")
        val isAuthenticated: Boolean,
        @SerializedName("refreshToken")
        val refreshToken: String,
        @SerializedName("accessToken")
        val accessToken: String
)
