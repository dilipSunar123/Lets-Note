package com.example.ikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SQLiteDatabase database;
    CustomNoteAdapter customNoteAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.addNoteActivity) {
            Intent intent = new Intent(MainActivity.this, AddNewNote_Activity.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.deleteAllnotes) {
            deleteAll();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteHelper helper = new NoteHelper(MainActivity.this);
        database = helper.getWritableDatabase();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        customNoteAdapter = new CustomNoteAdapter(this, getAllNotes());
        recyclerView.setAdapter(customNoteAdapter);

        if (customNoteAdapter.getItemCount() == 0) {
            Toast.makeText(this, "No notes found!", Toast.LENGTH_SHORT).show();
        }

        // swipe delete element
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeNote( (Long) viewHolder.itemView.getTag());
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        // floating action button
        FloatingActionButton fab = findViewById(R.id.floatingBtn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddNewNote_Activity.class);
                startActivity(intent);
            }
        });
    }

    public Cursor getAllNotes() {
        return database.query(
                DataCollectionClass.Collection.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataCollectionClass.Collection._ID + " DESC"
        );
    }

    public void removeNote(Long id) {
        database.delete(DataCollectionClass.Collection.TABLE_NAME,
                DataCollectionClass.Collection._ID + "=" + id,
                null);
        customNoteAdapter.swapCursor(getAllNotes());
    }

    public void deleteAll () {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Delete all notes?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.delete(DataCollectionClass.Collection.TABLE_NAME, null, null);
                        Toast.makeText(MainActivity.this, "All notes deleted successfully", Toast.LENGTH_SHORT).show();
                        customNoteAdapter.swapCursor(getAllNotes());
                    }
                }).setNegativeButton("Cancel", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}