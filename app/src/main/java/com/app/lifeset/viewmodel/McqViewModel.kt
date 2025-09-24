package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.GKResponse
import com.app.lifeset.model.McqResponse
import com.app.lifeset.repositories.GKRepositories
import com.app.lifeset.repositories.McqRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class McqViewModel @Inject constructor(val repositories: McqRepositories) : ViewModel() {
    val isLoading = MutableLiveData(false)
    val mcqLiveData = MutableLiveData<McqResponse>()

    fun getMcqData(language:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getMcqData(language).let {
                if (it.body() != null) {
                    mcqLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}
