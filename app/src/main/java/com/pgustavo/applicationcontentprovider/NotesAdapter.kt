package com.pgustavo.applicationcontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pgustavo.applicationcontentprovider.databese.NoteDataBaseHelper.Companion.DESCRIPTION_NOTES
import com.pgustavo.applicationcontentprovider.databese.NoteDataBaseHelper.Companion.TITLE_NOTES

class NotesAdapter (private val listener: NoteClickedListener ): RecyclerView.Adapter<NotesAdapter.NotesAdapterViewHolder>() {

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesAdapterViewHolder {
        return NotesAdapterViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: NotesAdapterViewHolder, position: Int) {
        mCursor?.moveToPosition(position)

        holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.noteDescription.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)
        holder.noteButtonRemove.setOnClickListener {
            mCursor?.moveToPosition(position)
            listener.noteRemoveItem(mCursor)
           }
        holder.itemView.setOnClickListener {listener.noteClickedItem(mCursor as Cursor)}
    }

    override fun getItemCount(): Int = if (mCursor != null) mCursor?.count as Int else 0

    fun setCursor(newCursor: Cursor?) {
        mCursor = newCursor
        notifyDataSetChanged()
    }

    inner class NotesAdapterViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
        val noteTitle = itemView.findViewById(R.id.note_title) as TextView
        val noteDescription = itemView.findViewById(R.id.note_description) as TextView
        val noteButtonRemove = itemView.findViewById(R.id.note_button_remove) as Button
    }

}


