package com.app.lifeset.repositories

import com.app.lifeset.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getProfile(uid: String) = apiService.getProfilePic(uid)

    suspend fun getProfileDetail(uid: String) = apiService.getProfileDetail(uid)

    suspend fun getCollageName(id: String) = apiService.getCollageName(id)

    suspend fun getCasts(uid: String) = apiService.getCaste(uid)

    suspend fun updateProfilePic(uid: RequestBody, profilePath: MultipartBody.Part?) =
        apiService.updateProfile(
            uid, profilePath
        )

    suspend fun getPageDescription(page: String) = apiService.getPageDescription(page)


    suspend fun doUpdateProfile(
        uid: String,
        name: String,
        mobile: String,
        email: String,
        state: String,
        city: String,
        pincode: String,
        address: String,
        gender: String,
        religion: String,
        category: String,
        year: String,
        profile: String,
        langauge: String
    ) = apiService.doUpdateProfile(
        uid, name, mobile, email, state, city, pincode, address,
        gender, religion, category, year, profile,
        langauge
    )
}