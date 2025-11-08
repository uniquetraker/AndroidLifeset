package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.BaseResponse
import com.app.lifeset.model.GKResponse
import com.app.lifeset.model.McqRequest
import com.app.lifeset.model.McqResponse
import com.app.lifeset.model.SaveMcqResponse
import com.app.lifeset.repositories.GKRepositories
import com.app.lifeset.repositories.McqRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class McqViewModel @Inject constructor(val repositories: McqRepositories) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val mcqLiveData = MutableLiveData<McqResponse>()
    val saveMcqLiveData = MutableLiveData<SaveMcqResponse>()

    fun getMcqData(language:String,uid: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getMcqData(language,uid).let {
                if (it.body() != null) {
                    mcqLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }

    fun saveMcqData(request: McqRequest) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.saveMcqData(request).let {
                if (it.body() != null) {
                    saveMcqLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }
}
