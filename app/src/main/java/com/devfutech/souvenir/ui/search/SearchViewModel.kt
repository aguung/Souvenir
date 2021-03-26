package com.devfutech.souvenir.ui.search

import androidx.lifecycle.*
import com.devfutech.souvenir.data.local.SouvenirDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val db: SouvenirDatabase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel(), LifecycleObserver {

    companion object {
        const val KEY_SEARCH = "subsearch"
        const val DEFAULT_SEARCH = "%%"
        const val KEY_TYPE = "keytype"
    }

    private val clearListCh = Channel<Unit>(Channel.CONFLATED)

    init {
        if (!savedStateHandle.contains(KEY_SEARCH)) {
            savedStateHandle.set(KEY_SEARCH, DEFAULT_SEARCH)
            savedStateHandle.set(KEY_TYPE, "Semua")
        }
    }

    val category = db.souvenirDao().getCategory()

    val souvenirFlow = flowOf(
        clearListCh.receiveAsFlow().map { listOf() },
        savedStateHandle.getLiveData<String>(KEY_SEARCH)
            .asFlow()
            .combine(savedStateHandle.getLiveData<String>(KEY_TYPE).asFlow()) { search, type ->
                arrayOf(search, type)
            }.flatMapLatest {
                if (it[1] == "Semua") {
                    db.souvenirDao().getAllDataSearch(search = it[0])
                } else {
                    db.souvenirDao().getAllPagedByCategoryAndSearch(category = it[1], search = it[0])
                }
            }
    ).flattenMerge(2)

    fun shouldShowSearch(
        subsearch: String
    ) = savedStateHandle.get<String>(KEY_SEARCH) != subsearch

    fun shouldShowType(
        type: String
    ) = savedStateHandle.get<String>(KEY_TYPE) != type

    fun showSearch(type: String, search: String) {
        if (!shouldShowSearch(search) && !shouldShowType(type)) return
        clearListCh.offer(Unit)
        savedStateHandle.set(KEY_SEARCH, search)
        savedStateHandle.set(KEY_TYPE, type)
    }

}