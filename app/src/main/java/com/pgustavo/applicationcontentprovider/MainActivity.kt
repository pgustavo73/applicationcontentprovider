package com.pgustavo.applicationcontentprovider

import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.pgustavo.applicationcontentprovider.databese.NoteDataBaseHelper.Companion.TITLE_NOTES
import com.pgustavo.applicationcontentprovider.databese.NoteProvider.Companion.URI_NOTES

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    lateinit var noteRecyclerView: RecyclerView
    lateinit var noteAdd: FloatingActionButton

    lateinit var adapterNote: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteAdd = findViewById(R.id.note_add)
        noteAdd.setOnClickListener {
            NoteDetailFragment().show(supportFragmentManager, "dialog")
        }

        setupAdapter()
        setupRecyclerView()
    }

     private fun setupAdapter() {
         adapterNote = NotesAdapter(object : NoteClickedListener {
             override fun noteClickedItem(cursor: Cursor) {
                 val id = cursor?.getLong(cursor.getColumnIndex(_ID))
                 val fragment = NoteDetailFragment.newInstance(id)
                 fragment.show(supportFragmentManager, "dialog")
             }

             override fun noteRemoveItem(cursor: Cursor?) {
                 val id: Long? = cursor?.getLong(cursor.getColumnIndex(_ID))
                 contentResolver.delete(Uri.withAppendedPath(URI_NOTES, id.toString()), null, null)
             }

         })
         adapterNote.apply {
             setHasStableIds(true)
         }
     }

    private fun setupRecyclerView() {
        noteRecyclerView = findViewById(R.id.notes_recycler)
        noteRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = adapterNote
            hasFixedSize()
        }
        LoaderManager.getInstance(this).initLoader(0, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this, URI_NOTES, null, null, null, TITLE_NOTES)

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if (data != null) { adapterNote.setCursor(data) }
        }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapterNote.setCursor(null)
    }
}