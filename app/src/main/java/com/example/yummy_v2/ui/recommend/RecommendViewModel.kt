package com.example.yummy_v2.ui.recommend

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.data.local.PlaceDatabase
import com.example.yummy_v2.data.remote.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor (application: Application, private val repository: PlaceRepository) : AndroidViewModel(application) {
    private val _restaurants : MutableLiveData<List<Place>> = MutableLiveData()
    val restaurants get() = _restaurants

    fun insert(place: Place) = viewModelScope.launch {
        repository.insert(place)
    }

    fun update(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(place)
        }
    }

    fun delete(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(place)
        }
    }

    fun getAll() {
        viewModelScope.launch(Dispatchers.IO) {
            _restaurants.postValue(repository.getAll())
        }
    }
}