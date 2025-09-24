package com.app.lifeset.network

import com.app.lifeset.model.AddReferalRequest
import com.app.lifeset.model.AddReferalResponse
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.CMSPageResponse
import com.app.lifeset.model.CasteResponse
import com.app.lifeset.model.CategoryResponse
import com.app.lifeset.model.ChangeCollageResponse
import com.app.lifeset.model.ChatStudentResponse
import com.app.lifeset.model.CollageNameResponse
import com.app.lifeset.model.CollageResponse
import com.app.lifeset.model.CourseResponse
import com.app.lifeset.model.DeleteNotification
import com.app.lifeset.model.DeleteReferalResponse
import com.app.lifeset.model.EducationInformationRequest
import com.app.lifeset.model.EventResponse
import com.app.lifeset.model.ExamDetailRequest
import com.app.lifeset.model.ExamDetailResponse
import com.app.lifeset.model.ExamResponse
import com.app.lifeset.model.FreelancerJobsResponse
import com.app.lifeset.model.FreelancerStatusResponse
import com.app.lifeset.model.GKResponse
import com.app.lifeset.model.GeneralKnowledgeResponse
import com.app.lifeset.model.HomeTownRequest
import com.app.lifeset.model.InviteStudentResponse
import com.app.lifeset.model.JobDetailResponse
import com.app.lifeset.model.JobResponse
import com.app.lifeset.model.LoginResponse
import com.app.lifeset.model.McqResponse
import com.app.lifeset.model.NotificationResponse
import com.app.lifeset.model.OtpResponse
import com.app.lifeset.model.PersonalInformationModel
import com.app.lifeset.model.PersonalityDataResponse
import com.app.lifeset.model.PersonalityRequest
import com.app.lifeset.model.PersonalityResponse
import com.app.lifeset.model.ProfileModel
import com.app.lifeset.model.ReferalResponse
import com.app.lifeset.model.RegisterResponse
import com.app.lifeset.model.SaveFreelancerDetailResponse
import com.app.lifeset.model.SaveFreelancerRequest
import com.app.lifeset.model.SettingsDateResponse
import com.app.lifeset.model.SkillsRequest
import com.app.lifeset.model.StudentProfileResponse
import com.app.lifeset.model.UpdateProfileModel
import com.app.lifeset.model.UpdateUserResponse
import com.app.lifeset.model.chat.ChatHistoryResponse
import com.app.lifeset.model.chat.ChatResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("api_login")
    suspend fun doLogin(
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("fcm_token") fcmToken: String
    ): Response<LoginResponse>

    @GET("api_get_college_list")
    suspend fun getCollage(
    ): Response<CollageResponse>

    @FormUrlEncoded
    @POST("api_update_user_data")
    suspend fun getUpdateUserData(
        @Field("mobile") mobile: String,
        @Field("college_id") college_id: String,
        @Field("new_college") new_college: String,
        @Field("stream") stream: String,
        @Field("course") course: String,
        @Field("year") year: String,
        @Field("collage_name") collage_name: String
    ): Response<UpdateUserResponse>

    @FormUrlEncoded
    @POST("api_register")
    suspend fun doRegister(
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("name") name: String,
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("api_verify_register")
    suspend fun doVerifyRegister(
        @Field("otp") otp: String,
        @Field("email") email: String,
        @Field("mobile") mobile: String,
        @Field("name") name: String,
        @Field("token") token: String,
        @Field("fcm_token") fcm_token: String
    ): Response<OtpResponse>

    @GET("api_get_category_data")
    suspend fun getCategory(
        @Query("college") college: String,
        @Query("stream") stream: String
    ): Response<CategoryResponse>

    @GET("api_get_course_data")
    suspend fun getYear(
        @Query("course") course: String
    ): Response<CourseResponse>

    @GET("api_change_college")
    suspend fun getChangeCollage(
        @Query("college") college: String
    ): Response<ChangeCollageResponse>

    @FormUrlEncoded
    @POST("api_forgot_password")
    suspend fun doForgotPassword(@Field("mobile") mobile: String): Response<BaseResponse>

    @FormUrlEncoded
    @POST("api_change_password")
    suspend fun doChangePassword(
        @Field("uid") uid: String,
        @Field("cnfpassword") cnfpassword: String
    ): Response<BaseResponse>


    @GET("api_get_wall_list")
    suspend fun getJobList(
        @Query("jb_type") jb_type: String,
        @Query("uid") uid: String
    ): Response<JobResponse>

    @GET("api_applied_post")
    suspend fun applyPost(
        @Query("college_id") college_id: String,
        @Query("uid") uid: String,
        @Query("post_id") post_id: String
    ): Response<BaseResponse>

    @GET("api_applied_wall_list")
    suspend fun getAppliedJobs(
        @Query("college_id") college_id: String,
        @Query("uid") uid: String,
    ): Response<JobResponse>

    @GET("api_bookmarked_post")
    suspend fun addBookMark(
        @Query("college_id") college_id: String,
        @Query("uid") uid: String,
        @Query("post_id") post_id: String
    ): Response<BaseResponse>

    @FormUrlEncoded
    @POST("api_get_profile_pic")
    suspend fun getProfilePic(
        @Field("uid") uid: String
    ): Response<ProfileModel>

    @GET("api_get_general_profile_detail")
    suspend fun getProfileDetail(
        @Query("uid") uid: String
    ): Response<StudentProfileResponse>

    @GET("api_get_student_profile_data")
    suspend fun getCaste(@Query("uid") uid: String): Response<CasteResponse>

    @FormUrlEncoded
    @POST("api_update_general_profile_detail")
    suspend fun doUpdateProfile(
        @Field("uid") uid: String,
        @Field("name") name: String,
        @Field("mobile") mobile: String,
        @Field("email") email: String,
        @Field("state") state: String,
        @Field("city") city: String,
        @Field("pincode") pincode: String,
        @Field("address") address: String,
        @Field("gender") gender: String,
        @Field("religion") religion: String,
        @Field("caste") caste: String,
        @Field("year") year: String,
        @Field("profile") profile: String,
        @Field("language") language: String
    ): Response<BaseResponse>

    @GET("api_job_detail")
    suspend fun getJobDetail(
        @Query("uid") uid: String,
        @Query("id") id: String
    ): Response<JobDetailResponse>

    @Multipart
    @POST("api_update_profile_pic")
    suspend fun updateProfile(
        @Part("uid") uid: RequestBody,
        @Part image: MultipartBody.Part?
    ): Response<ProfileModel>

    @GET("api_get_page_description")
    suspend fun getPageDescription(
        @Query("page") page: String
    ): Response<CMSPageResponse>

    @GET("api_get_mobile_notification_list")
    suspend fun getNotificationList(@Query("mobile") mobile: String): Response<NotificationResponse>

    @POST("api_get_delete_notification")
    suspend fun deleteNotifications(
        @Query("id") id: String,
        @Query("mobile") mobile: String
    ): Response<DeleteNotification>

    @POST("api_update_member_profile")
    suspend fun getUpdateProfile(@Body request: SkillsRequest): Response<UpdateProfileModel>

    @POST("api_update_member_profile")
    suspend fun getPersonalInformation(@Body request: PersonalInformationModel): Response<UpdateProfileModel>

    @POST("api_update_member_profile")
    suspend fun getEducationInformation(@Body educationInformationRequest: EducationInformationRequest)
            : Response<UpdateProfileModel>

    @POST("api_update_member_profile")
    suspend fun updateHomeTown(
        @Body request: HomeTownRequest
    ): Response<UpdateProfileModel>


    @GET("api_get_college_name")
    suspend fun getCollageName(
        @Query("id") id: String
    ): Response<CollageNameResponse>

    @GET("chat/searchStudent")
    suspend fun getChatStudents(
        @Query("uid") uid: String,
        @Query("search") search: String,
        @Query("page") page: String
    ): Response<ChatStudentResponse>

    @FormUrlEncoded
    @POST("chat/inviteStudent")
    suspend fun inviteStudent(
        @Field("uid") uid: String,
        @Field("request_to") requestTo: String
    ): Response<BaseResponse>

    @GET("chat/inviteStudentsList")
    suspend fun getInviteList(
        @Query("uid") uid: String,
        @Query("page") page: String
    ): Response<InviteStudentResponse>

    @GET("chat/myNetworkList")
    suspend fun getMyNetworks(
        @Query("uid") uid: String,
        @Query("page") page: String
    ): Response<InviteStudentResponse>

    @FormUrlEncoded
    @POST("chat/inviteStatus")
    suspend fun inviteStatus(
        @Field("uid") uid: String,
        @Field("invite_id") invite_id: String,
        @Field("status") status: String
    ): Response<BaseResponse>

    @GET("chat/studentChatList")
    suspend fun getChat(
        @Query("uid") uid: String,
        @Query("page") page: String
    ): Response<ChatResponse>

    @GET("chat/studentChatHistory")
    suspend fun getStudentChatHistory(
        @Query("uid") uid: String,
        @Query("receiver_id") receiver_id: String,
        @Query("page") page: String
    ): Response<ChatHistoryResponse>

    @FormUrlEncoded
    @POST("chat/storeChatMessage")
    suspend fun sendMessage(
        @Field("uid") uid: String,
        @Field("receiver_id") receiver_id: String,
        @Field("comments") comments: String
    ): Response<BaseResponse>

    @GET("freelancer/jobs")
    suspend fun getFreelancerJobs(
        @Query("uid") uid: String,
        @Query("search") search: String,
        @Query("page") page: String
    ): Response<FreelancerJobsResponse>


    @GET("freelancer/statusList")
    suspend fun getFreelancerStatus(): Response<FreelancerStatusResponse>

    @POST("save-freelancer-detail")
    suspend fun saveFreelancerDetails(
        @Body request: SaveFreelancerRequest
    ): Response<SaveFreelancerDetailResponse>


    @FormUrlEncoded
    @POST("freelancer/job/updateStatus")
    suspend fun updateJobStatus(
        @Field("uid") uid: String,
        @Field("id") id: String,
        @Field("status") status: String
    ): Response<BaseResponse>

    @GET("lifeset-wall/exams_data")
    suspend fun getExamData(
        @Query("uid") uid: String
    ): Response<ExamResponse>

    @GET("lifeset-wall/exam_detail")
    suspend fun getExamDetails(
        @Query("uid") uid: String,
        @Query("postid") postId: String
    ): Response<ExamDetailResponse>

    @GET("lifeset-wall/events_data")
    suspend fun getEventData(): Response<EventResponse>

    @GET("lifeset-wall/gk_data")
    suspend fun getGKData(): Response<GKResponse>

    @GET("lifeset-wall/interested_post")
    suspend fun postInterested(
        @Query("post_id") post_id: String,
        @Query("uid") uid: String
    ): Response<BaseResponse>

    @GET("lifeset-wall/settingMobileDateUpdate")
    suspend fun getSettingsMobileUpdatedDate(
        @Query("uid") uid: String
    ): Response<SettingsDateResponse>

    @GET("lifeset-wall/personality_data")
    suspend fun getPersonalityData(
        @Query("uid") uid: String
    ): Response<PersonalityResponse>

    @POST("save-personality-answer")
    suspend fun doPersonalityAnswer(
        @Body request: PersonalityRequest
    ): Response<PersonalityDataResponse>

    @GET("api/referral-list")
    suspend fun getReferalList(
        @Query("std_id") std_id: String
    ): Response<ReferalResponse>

    @POST("add-referral")
    suspend fun addReferal(
        @Body request: AddReferalRequest
    ):Response<AddReferalResponse>

    @FormUrlEncoded
    @POST("delete-referral")
    suspend fun deleteReferal(
        @Field("std_id")std_id:String,
        @Field(
            "referral_id"
        )referral_id:String
    ):Response<DeleteReferalResponse>

    @GET("get_post_gk_data")
    suspend fun getGeneralKnowledgeData(
        @Query("langu")langu:String
    ):Response<GeneralKnowledgeResponse>

    @GET("get_mcq_data")
    suspend fun getMcqData(
        @Query("langu")langu:String
    ):Response<McqResponse>
}