package resumade.kshitijchauhan.haroldadmin.com.resumade.remote

import io.reactivex.Single
import okhttp3.RequestBody
import resumade.kshitijchauhan.haroldadmin.com.resumade.remote.response.GenerationResponse
import resumade.kshitijchauhan.haroldadmin.com.resumade.remote.response.ValidationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ResumadeService {

    /**
     * Validate resume
     *
     * This endpoint is used to query the server about the validity
     * of the given resume schema
     * @param jsonReq Json Request Body containing resume schema
     * @return [ValidationResponse] A response object
     */
    @POST("/validation")
    fun validate(@Body jsonReq: RequestBody): Single<ValidationResponse>

    /**
     * Generate resume from requested JSON
     *
     * This endpoint is used to get generated HTML from the given
     * resume schema.
     * @param jsonReq Json Request Body containing resume schema
     * @return [GenerationResponse] A response object
     */
    @POST("/generation")
    fun generate(@Body jsonReq: RequestBody): Single<GenerationResponse>

}