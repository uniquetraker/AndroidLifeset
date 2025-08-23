package com.app.lifeset.repositories

import com.app.lifeset.model.EducationInformationRequest
import com.app.lifeset.model.HomeTownRequest
import com.app.lifeset.model.PersonalInformationModel
import com.app.lifeset.model.SkillsRequest
import com.app.lifeset.network.ApiService
import javax.inject.Inject

class UpdateProfileRepositories @Inject constructor(val apiService: ApiService) {

    suspend fun getUpdateProfile(request: SkillsRequest) = apiService.getUpdateProfile(request)

    suspend fun getPersonalInformation(request: PersonalInformationModel) =
        apiService.getPersonalInformation(
            request
        )

    suspend fun getEducationInformation(request: EducationInformationRequest) =
        apiService.getEducationInformation(
            request
        )

    suspend fun updateHomeTown(request: HomeTownRequest)=apiService.updateHomeTown(
        request
    )
}