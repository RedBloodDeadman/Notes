package com.notes.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.notes.databinding.ListItemNoteBinding
import com.notes.domain.NoteListItem


open class NotesRecyclerViewAdapter :
    RecyclerView.Adapter<NotesRecyclerViewAdapter.ViewHolder>() {

    private val items = mutableListOf<NoteListItem>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ListItemNoteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(holder, items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(
        items: List<NoteListItem>
    ) {
        val diffResult = DiffUtil.calculateDiff(NotesDiffUtilCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getItem(position: Int) = this.items[position]

    open fun onItemClicked(item: NoteListItem) {
    }

    open fun onDeleteClicked(item: NoteListItem) {
    }

    inner class ViewHolder(
        private val binding: ListItemNoteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            viewHolder: ViewHolder,
            noteListItem: NoteListItem
        ) {
            binding.titleLabel.text = noteListItem.title
            binding.contentLabel.text = noteListItem.content

            binding.contentLayout.setOnClickListener {
                onItemClicked(getItem(viewHolder.adapterPosition))
            }

            binding.buttonDelete.setOnClickListener {
                onDeleteClicked(getItem(viewHolder.adapterPosition))
            }
        }
    }
}