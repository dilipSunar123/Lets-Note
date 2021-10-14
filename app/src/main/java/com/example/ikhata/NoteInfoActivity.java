package com.example.ikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NoteInfoActivity extends AppCompatActivity {

    private TextView title_info;
    private TextView desc_info;
    private TextView time_info;
    private Button updateBtn;
    private EditText tUpdateEditText;
    private EditText dUpdateEditText;

    SQLiteDatabase database;
    CustomNoteAdapter adapter;

    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_info);

        NoteHelper helper = new NoteHelper(this);
        database = helper.getWritableDatabase();
        adapter = new CustomNoteAdapter(this, getAllNotes());

        title_info = findViewById(R.id.title_info_view);
        desc_info = findViewById(R.id.desc_info_view);
        time_info = findViewById(R.id.time_info_view);
        updateBtn = findViewById(R.id.updateBtn);
        tUpdateEditText = findViewById(R.id.update_title_editText);
        dUpdateEditText = findViewById(R.id.update_desc_editText);

        // initially ediTTexts will remain disabled
        tUpdateEditText.setEnabled(false);
        dUpdateEditText.setEnabled(false);
        tUpdateEditText.setBackgroundColor(Color.TRANSPARENT);
        dUpdateEditText.setBackgroundColor(Color.TRANSPARENT);

        Intent intent = getIntent();
        final String title = intent.getStringExtra("title");
        final String desc = intent.getStringExtra("description");
        String time = intent.getStringExtra("time");
        id = intent.getStringExtra("id");

        title_info.setText(title);
        desc_info.setText(desc);
        time_info.setText(time);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                tUpdateEditText.setEnabled(true);
                dUpdateEditText.setEnabled(true);
                tUpdateEditText.setHint("Enter new title here...");
                dUpdateEditText.setHint("Enter new description here...");
                updateNote(id);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addNoteActivity) {
            Intent intent = new Intent(NoteInfoActivity.this, AddNewNote_Activity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNote(String id) {
        String title = tUpdateEditText.getText().toString();
        String desc = dUpdateEditText.getText().toString();

        if (title.trim().length() == 0)
            return;

        ContentValues cv = new ContentValues();
        cv.put(DataCollectionClass.Collection.COLUMN_NAME_TITLE, title);
        // if user leaves the desc editText empty then...
        if (desc.trim().length() == 0) cv.put(DataCollectionClass.Collection.COLUMN_NAME_DESC, "-");
        else if (desc.trim().length() > 0) cv.put(DataCollectionClass.Collection.COLUMN_NAME_DESC, desc);

        database.update(DataCollectionClass.Collection.TABLE_NAME, cv,
                DataCollectionClass.Collection._ID + " = ?", new String[]{id});
        adapter.swapCursor(getAllNotes());

        title_info.setText(title);
        desc_info.setText(desc);
        // clear editTexts after user enters the data and click the button.
        tUpdateEditText.getText().clear();
        dUpdateEditText.getText().clear();
        // disable editTexts after user successfully enter the notes.
        tUpdateEditText.setEnabled(false);
        dUpdateEditText.setEnabled(false);
        Toast.makeText(this, "Note updated successfully", Toast.LENGTH_SHORT).show();
    }

    public Cursor getAllNotes() {
        return database.query(DataCollectionClass.Collection.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                DataCollectionClass.Collection._ID + " DESC");
    }
}