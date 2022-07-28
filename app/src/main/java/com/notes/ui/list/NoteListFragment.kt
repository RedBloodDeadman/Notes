package com.notes.ui.list

import android.os.Bundle
import android.widget.LinearLayout
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import com.notes.databinding.FragmentNoteListBinding
import com.notes.di.DependencyManager.Companion.getAppComponent
import com.notes.domain.NoteListItem
import com.notes.ui._base.FragmentNavigator
import com.notes.ui._base.ViewBindingFragment
import com.notes.ui._base.findImplementationOrThrow
import com.notes.ui.details.NoteDetailsFragment
import com.notes.ui.details.NoteDetailsViewModel


class NoteListFragment : ViewBindingFragment<FragmentNoteListBinding>(
    FragmentNoteListBinding::inflate
) {
    private val viewModelNotesList: NoteListViewModel by activityViewModels {
        getAppComponent().viewModelsFactory()
    }

    private val viewModelNoteDetails: NoteDetailsViewModel by activityViewModels {
        getAppComponent().viewModelsFactory()
    }

    private lateinit var recyclerViewAdapter: NotesRecyclerViewAdapter

    override fun onViewBindingCreated(
        viewBinding: FragmentNoteListBinding,
        savedInstanceState: Bundle?
    ) {
        super.onViewBindingCreated(viewBinding, savedInstanceState)

        recyclerViewAdapter = object : NotesRecyclerViewAdapter() {
            override fun onItemClicked(item: NoteListItem) {
                super.onItemClicked(item)
                viewModelNotesList.onCreateNoteClick()
                viewModelNoteDetails.setNoteLiveData(item)
            }

            override fun onDeleteClicked(item: NoteListItem) {
                super.onDeleteClicked(item)
                viewModelNotesList.onDeleteClick(item)
            }
        }

        viewBinding.list.adapter = recyclerViewAdapter
        viewBinding.list.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayout.VERTICAL
            )
        )
        viewBinding.createNoteButton.setOnClickListener {
            viewModelNoteDetails.setNoteLiveData(null)
            viewModelNotesList.onCreateNoteClick()
        }

        viewModelNotesList.notesListLiveData.observe(
            viewLifecycleOwner
        ) {
            if (it != null) {
                recyclerViewAdapter.setItems(it.map { noteDbo ->
                    NoteListItem(
                        id = noteDbo.id,
                        title = noteDbo.title,
                        content = noteDbo.content,
                    )
                })
            }
        }
        viewModelNotesList.navigateToNoteCreation.observe(
            viewLifecycleOwner
        ) {
            findImplementationOrThrow<FragmentNavigator>()
                .navigateTo(
                    NoteDetailsFragment()
                )

        }
    }



}