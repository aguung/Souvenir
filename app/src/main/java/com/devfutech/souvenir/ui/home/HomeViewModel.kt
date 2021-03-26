package com.devfutech.souvenir.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.devfutech.souvenir.data.SouvenirRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class HomeViewModel @Inject constructor(repository: SouvenirRepository) : ViewModel() {
    val souvenir = repository.getSouvenir()
}