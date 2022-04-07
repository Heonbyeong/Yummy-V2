package com.example.yummy_v2.ui.recommend

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.yummy_v2.data.local.Place
import com.example.yummy_v2.data.local.PlaceDatabase
import com.example.yummy_v2.data.remote.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor (application: Application) : AndroidViewModel(application) {

    private val readAllData : LiveData<List<Place>>
    private val repository : PlaceRepository

    init {
        val placeDao = PlaceDatabase.getInstance(application)!!.placeDao()
        repository = PlaceRepository(placeDao)
        readAllData = repository.getAll
    }

    fun insert(place: Place){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(place)
        }
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

    fun getAll() = readAllData.value
}