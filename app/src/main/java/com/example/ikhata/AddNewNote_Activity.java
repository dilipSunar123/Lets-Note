package com.example.ikhata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNewNote_Activity extends AppCompatActivity {

    EditText title_editText;
    EditText desc_editText;
    Button addBtn;
    RecyclerView recyclerView;

    SQLiteDatabase database;
    CustomNoteAdapter customNoteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);

        NoteHelper helper = new NoteHelper(this);
        database = helper.getWritableDatabase();

        recyclerView = findViewById(R.id.recyclerView);
        title_editText = findViewById(R.id.title_editText);
        desc_editText = findViewById(R.id.desc_editText);
        addBtn = findViewById(R.id.addBtn);
        customNoteAdapter = new CustomNoteAdapter(this, getAllNotes());

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    public void addItem() {
        if (title_editText.getText().toString().trim().length() == 0) {
            Toast.makeText(this, "Please enter a valid input", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = title_editText.getText().toString();
        String desc = desc_editText.getText().toString();

        // store the date and time of storing data...
        Calendar calendar = Calendar.getInstance();
        /*
            h -> hour
            mm -> minute
            a -> am/pm
            MMMM -> Month(in words)
            d -> date(in digits)
            yyyy -> year
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a\nMMMM d, yyyy");
        String currentDateTime = dateFormat.format(calendar.getTime());

        ContentValues cv = new ContentValues();
        cv.put(DataCollectionClass.Collection.COLUMN_NAME_TITLE, title);
        if (desc.trim().length() == 0) {
            cv.put(DataCollectionClass.Collection.COLUMN_NAME_DESC, "-");
        } else if (desc.trim().length() > 0) {
            cv.put(DataCollectionClass.Collection.COLUMN_NAME_DESC, desc);
        }
        cv.put(DataCollectionClass.Collection.TIME, currentDateTime);

        database.insert(DataCollectionClass.Collection.TABLE_NAME, null, cv);
        customNoteAdapter.swapCursor(getAllNotes());

        Toast.makeText(this, "Note added successfully", Toast.LENGTH_SHORT).show();
        title_editText.getText().clear();
        desc_editText.getText().clear();
    }

    public Cursor getAllNotes () {
        return database.query(DataCollectionClass.Collection.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataCollectionClass.Collection._ID + " DESC");
    }
}