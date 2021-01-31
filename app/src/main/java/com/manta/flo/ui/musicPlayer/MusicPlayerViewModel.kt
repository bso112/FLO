package com.manta.flo.ui.musicPlayer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.manta.flo.model.SongResponse
import com.manta.flo.repository.MainRepository
import kotlinx.coroutines.launch

class MusicPlayerViewModel @ViewModelInject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _songLiveData: MutableLiveData<SongResponse> = MutableLiveData()
    val songLiveData: LiveData<SongResponse>
        get() = _songLiveData

    private val _toastLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: LiveData<String> get() = _toastLiveData

    fun getSongData() {
        viewModelScope.launch {
            repository.getSong().apply {
                if (isSuccessful)
                    _songLiveData.postValue(this.body())
                else
                    _toastLiveData.postValue(this.errorBody().toString());

            }

        }
    }


}