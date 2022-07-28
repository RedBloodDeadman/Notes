package com.notes.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.notes.domain.NoteListItem
import java.time.LocalDateTime

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY modifiedAt DESC")
    fun getAll(): LiveData<List<NoteDbo>>

    @Query("SELECT * FROM notes WHERE id=:id")
    fun getById(id: Long): NoteDbo?

    @Insert
    fun insertAll(vararg notes: NoteDbo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(note: NoteDbo)

    @Update
    fun update(note: NoteDbo)

    @Transaction
    fun insertOrUpdate(id: Long, note: NoteListItem) {
        val datetime = LocalDateTime.now()
        val result = getById(id)
        if (result != null) {
            update(
                NoteDbo(
                    result.id,
                    note.title,
                    note.content,
                    result.createdAt,
                    datetime
                ))
        } else {
            insert(
                NoteDbo(
                0,
                    note.title,
                    note.content,
                    datetime,
                    datetime
                ))
        }
    }

    @Delete
    fun delete(note: NoteDbo)

    fun deleteItem(id: Long){
        val result = getById(id)
        if (result != null) {
            delete(result)
        }
    }

}