package com.devfutech.souvenir.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.withTransaction
import com.devfutech.souvenir.data.local.SouvenirDatabase
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.data.network.SouvenirApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SouvenirRepository @Inject constructor(
    private val api: SouvenirApi,
    private val db: SouvenirDatabase
) {

    val souvenir = MutableLiveData<List<Souvenir>>()

    init {
        souvenir.observeForever {
            saveSouvenir(it)
        }
    }
    @ExperimentalPagingApi
    fun getSouvenir() = Pager(
        config = PagingConfig(10),
        remoteMediator = PagedKeyedRemoteMediator(api, db)
    ) {
        db.souvenirDao().getAllPaged()
    }.flow

//    suspend fun getSouvenir() = CoroutineScope(Dispatchers.IO).launch {
//        fetchSouvenir()
//    }

    private suspend fun fetchSouvenir() {
        val response = api.getSouvenir()
        souvenir.postValue(response.souvenirs)
    }

    private fun saveSouvenir(souvenir: List<Souvenir>) = CoroutineScope(Dispatchers.IO).launch {
        db.withTransaction {
            db.souvenirDao().insertAll(souvenir)
        }
    }
}