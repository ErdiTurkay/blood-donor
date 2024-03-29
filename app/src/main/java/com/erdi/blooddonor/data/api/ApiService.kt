package com.erdi.blooddonor.data.api

import com.erdi.blooddonor.data.api.request.ChangeLocationRequest
import com.erdi.blooddonor.data.api.request.ChangePasswordRequest
import com.erdi.blooddonor.data.api.request.ChangePhoneNumberRequest
import com.erdi.blooddonor.data.api.request.CreateNewPostRequest
import com.erdi.blooddonor.data.api.request.LoginRequest
import com.erdi.blooddonor.data.api.request.RegisterRequest
import com.erdi.blooddonor.data.api.request.ReplyPostRequest
import com.erdi.blooddonor.data.api.request.SendNotificationTokenRequest
import com.erdi.blooddonor.data.api.response.* // ktlint-disable no-wildcard-imports
import com.erdi.blooddonor.utils.APIConstants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST(APIConstants.LOGIN_URL)
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<AuthResponse>

    @PUT(APIConstants.REGISTER_URL)
    suspend fun registerUser(@Body registerRequest: RegisterRequest): Response<AuthResponse>

    @POST(APIConstants.UPDATE_PASSWORD_URL)
    suspend fun changePassword(@Body changePasswordRequest: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST(APIConstants.UPDATE_PHONE_NUMBER_URL)
    suspend fun changePhoneNumber(@Body changePhoneNumberRequest: ChangePhoneNumberRequest): Response<ChangePhoneNumberResponse>

    @POST(APIConstants.UPDATE_LOCATION)
    suspend fun changeLocation(@Body changeLocationRequest: ChangeLocationRequest): Response<ChangeLocationResponse>

    @GET(APIConstants.GET_ALL_POSTS)
    suspend fun getAllPosts(): Response<AllPostsResponse>

    @GET(APIConstants.GET_MY_POSTS)
    suspend fun getMyPosts(): Response<GetMyPostsResponse>

    @GET(APIConstants.POST_WITH_ID)
    suspend fun getOnePost(@Path("postId") postId: String): Response<GetOnePostResponse>

    @GET(APIConstants.POST_WITH_CITY)
    suspend fun getPostWithCity(@Path("cityName") cityName: String): Response<GetMyPostsResponse>

    @GET(APIConstants.POST_WITH_DISTRICT)
    suspend fun getPostWithDistrict(@Query("city") cityName: String, @Query("district") districtName: String): Response<GetMyPostsResponse>

    @DELETE(APIConstants.POST_WITH_ID)
    suspend fun deletePost(@Path("postId") postId: String): Response<DeletePostResponse>

    @POST(APIConstants.CREATE_NEW_POST)
    suspend fun createNewPost(@Body createNewPostRequest: CreateNewPostRequest): Response<CreateNewPostResponse>

    @POST(APIConstants.REPLY_POST)
    suspend fun replyPost(@Body replyPostRequest: ReplyPostRequest): Response<ReplyPostResponse>

    @POST(APIConstants.SEND_NOTIFICATION_TOKEN)
    suspend fun sendNotificationToken(@Body sendNotificationTokenRequest: SendNotificationTokenRequest): Response<SendNotificationTokenResponse>

    @GET(APIConstants.GET_NOTIFICATION_TOKENS)
    suspend fun getNotificationTokens(@Path("cityName") city: String, @Path("districtName") district: String): Response<GetNotificationTokens>
}
