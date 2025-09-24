package com.app.lifeset.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.lifeset.model.GKResponse
import com.app.lifeset.model.GeneralKnowledgeResponse
import com.app.lifeset.repositories.GKRepositories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GkViewModel @Inject constructor(val repositories: GKRepositories) : ViewModel() {

    val isLoading = MutableLiveData(false)
    val gkLiveData = MutableLiveData<GKResponse>()
    val generalKnowledgeLiveData = MutableLiveData<GeneralKnowledgeResponse>()

    fun getGKData() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getGKData().let {
                if (it.body() != null) {
                    gkLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


    fun getGeneralKnowledgeData(langauge:String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            repositories.getGeneralKnowledgeData(langauge).let {
                if (it.body() != null) {
                    generalKnowledgeLiveData.postValue(it.body())
                    isLoading.postValue(false)
                } else {
                    isLoading.postValue(false)
                }
            }
        }
    }


}