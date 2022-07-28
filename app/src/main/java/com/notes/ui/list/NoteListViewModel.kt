package com.notes.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notes.data.NoteDatabase
import com.notes.data.NoteDbo
import com.notes.domain.NoteListItem
import com.notes.utils.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class NoteListViewModel @Inject constructor(
    private val noteDatabase: NoteDatabase
) : ViewModel() {
    private val _navigateToNoteCreation = SingleLiveEvent<Unit?>()
    val navigateToNoteCreation: LiveData<Unit?> = _navigateToNoteCreation

    init {
    }

    val notesListLiveData: LiveData<List<NoteDbo>> by lazy {
        noteDatabase.noteDao().getAll()
    }

    fun onCreateNoteClick() {
        _navigateToNoteCreation.postValue(Unit)
    }

    fun onDeleteClick(item: NoteListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            noteDatabase.noteDao().deleteItem(item.id)
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}
