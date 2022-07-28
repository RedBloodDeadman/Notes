package com.notes.ui.details

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.notes.R
import com.notes.databinding.FragmentNoteDetailsBinding
import com.notes.di.DependencyManager
import com.notes.domain.NoteListItem
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui.list.NoteListViewModel

class NoteDetailsFragment() :
    ViewBindingFragment<FragmentNoteDetailsBinding>(
        FragmentNoteDetailsBinding::inflate
    ) {
    private lateinit var noteItem: NoteListItem

    private val viewModelNoteDetails: NoteDetailsViewModel by activityViewModels {
        DependencyManager.getAppComponent().viewModelsFactory()
    }

    private val viewModelNotesList: NoteListViewModel by activityViewModels {
        DependencyManager.getAppComponent().viewModelsFactory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewBinding != null && ::noteItem.isInitialized) {
            noteItem.title = viewBinding!!.editTitle.text.toString()
            noteItem.content = viewBinding!!.editText.text.toString()
            viewModelNoteDetails.setNoteLiveData(noteItem)
        }
    }

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteDetailsBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)
        setUpToolbar(viewBinding)

        viewModelNoteDetails.note.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                viewBinding.editTitle.setText(it.title)
                viewBinding.editText.setText(it.content)
                noteItem = it
            } else {
                viewBinding.editText.setText("")
                viewModelNotesList.notesListLiveData.observe(
                    viewLifecycleOwner
                ) { noteList ->
                    var noteName = "Note "
                    if (noteList != null) {
                        noteName += noteList.size + 1
                    } else {
                        noteName += "1"
                    }
                    viewBinding.editTitle.setText(noteName)
                }
            }
        }
    }

    private fun setUpToolbar(viewBinding: FragmentNoteDetailsBinding) {
        viewBinding.toolbar.title = "Editor"
        viewBinding.toolbar.inflateMenu(R.menu.note_details_menu)
        viewBinding.toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

        viewBinding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveButton -> {
                    saveCalled()
                    requireActivity().onBackPressed()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun saveCalled() {
        if (viewBinding != null) {
            if (::noteItem.isInitialized) {
                viewModelNoteDetails.onNoteSaveClicked(
                    NoteListItem(
                        noteItem.id,
                        viewBinding!!.editTitle.text.toString(),
                        viewBinding!!.editText.text.toString(),
                    )
                )
            } else {
                viewModelNoteDetails.onNoteSaveClicked(
                    NoteListItem(
                        0,
                        viewBinding!!.editTitle.text.toString(),
                        viewBinding!!.editText.text.toString(),
                    )
                )
            }
        }
    }
}