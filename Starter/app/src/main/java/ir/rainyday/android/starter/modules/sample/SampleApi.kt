package ir.rainyday.android.starter.modules.sample

import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface SampleApi {
    @GET("Sample")
    fun getAll(): Single<List<SampleJson>>

    @GET("Sample/{id}")
    fun get(@Path("id") id: Long): Single<SampleJson>

    @DELETE("Sample/{id}")
    fun delete(@Path("id") id: Long): Single<ResponseBody>

    @POST("Sample")
    fun insert(@Body body: SampleJson): Single<ResponseBody>

    @PUT("Sample/{id}")
    fun update(@Path("id") id: Long, @Body body: SampleJson): Single<ResponseBody>

    @Multipart
    @POST("Sample/Upload/{id}")
    fun upload(@Path("id") orderId: Long, @Part multipartBody: MultipartBody.Part): Single<ResponseBody>
}