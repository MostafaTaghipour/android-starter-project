package ${appPackage}.data.remote

import ${appPackage}.data.model.json.${moduleName}Json
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ${moduleName}Api {
    @GET("${moduleName}")
    fun getAll(): Single<List<${moduleName}Json>>

    @GET("${moduleName}/{id}")
    fun get(@Path("id") id: Long): Single<${moduleName}Json>

    @DELETE("${moduleName}/{id}")
    fun delete(@Path("id") id: Long): Single<ResponseBody>

    @POST("${moduleName}")
    fun insert(@Body body: ${moduleName}Json): Single<ResponseBody>

    @PUT("${moduleName}/{id}")
    fun update(@Path("id") id: Long, @Body body: ${moduleName}Json): Single<ResponseBody>

    @Multipart
    @POST("${moduleName}/Upload/{id}")
    fun upload(@Path("id") orderId: Long, @Part multipartBody: MultipartBody.Part): Single<ResponseBody>
}
