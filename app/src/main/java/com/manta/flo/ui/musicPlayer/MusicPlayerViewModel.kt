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


    //이렇게만하면 _songLiveData를 외부에서 참조해서 postValue 같은걸 쓸 수 가 있다.
    //설계상 좋지 않음.
    // val _songLiveData: MutableLiveData<SongResponse> = MutableLiveData()

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