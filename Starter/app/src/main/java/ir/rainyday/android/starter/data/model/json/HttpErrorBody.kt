package ir.rainyday.android.starter.data.model.json
import com.google.gson.annotations.SerializedName



data class HttpErrorBody(
		@SerializedName("DeveloperMessage") val developerMessage: String, //Verbose, plain language description of the problem. Provide developers suggestions about how to solve their problems here
		@SerializedName("UserMessage") val userMessage: String, //This is a message that can be passed along to end-users, if needed.
		@SerializedName("ErrorCode") val errorCode: Int, //100
		@SerializedName("MoreInfo") val moreInfo: String //http://tests.org/error/100
)