package com.notes.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.domain.NoteListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteDetailsViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {
    private val _note = MutableLiveData<NoteListItem?>()
    val note: LiveData<NoteListItem?> = _note

    init {
    }

    fun setNoteLiveData(item: NoteListItem?) {
        _note.postValue(item)
    }

    fun onNoteSaveClicked(
        item: NoteListItem
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().insertOrUpdate(
                item.id, item
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}