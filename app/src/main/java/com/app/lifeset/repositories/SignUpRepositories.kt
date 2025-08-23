package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import javax.inject.Inject


class SignUpRepositories @Inject constructor(val apiService: ApiService) {
    suspend fun getCollage() = apiService.getCollage()

    suspend fun updateUserData(
        mobile: String,
        collageId: String,
        newCollage: String,
        stream: String,
        course: String,
        year: String,
        collageName:String
    ) = apiService.getUpdateUserData(mobile, collageId, newCollage, stream, course, year,
        collageName)

    suspend fun getChangeCollage(collage: String) = apiService.getChangeCollage(collage)

    suspend fun getCategory(collageId: String, stream: String) =
        apiService.getCategory(collageId, stream)

    suspend fun getYear(courseId: String) = apiService.getYear(courseId)

    suspend fun doRegister(
        email: String, mobile: String,
        name: String,
    ) = apiService.doRegister(
        email, mobile, name
    )

    suspend fun doVerifyRegister(
        otp: String,
        email: String, mobile: String,
         name: String,
        token: String,
        fcmToken:String
    ) = apiService.doVerifyRegister(
        otp,
        email, mobile, name,  token,
        fcmToken
    )

}