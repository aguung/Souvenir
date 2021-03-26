package com.devfutech.souvenir.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.devfutech.souvenir.data.local.SouvenirDatabase
import com.devfutech.souvenir.data.local.entity.Souvenir
import com.devfutech.souvenir.data.network.SouvenirApi
import retrofit2.HttpException
import java.io.IOException

@ExperimentalPagingApi
class PagedKeyedRemoteMediator(
    private val api: SouvenirApi,
    private val database: SouvenirDatabase
) : RemoteMediator<Int, Souvenir>() {
    val souvenir = database.souvenirDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Souvenir>): MediatorResult {
        return try {
            when (loadType) {
                LoadType.PREPEND ->
                    return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    lastItem.souvenirCode
                }
                LoadType.REFRESH -> println("Refresh")
            }
            val response = api.getSouvenir()
            database.withTransaction {
                souvenir.insertAll(response.souvenirs)
            }

            MediatorResult.Success(
                endOfPaginationReached = true
            )
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }
}