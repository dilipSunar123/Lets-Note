package com.example.ikhata;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomNoteAdapter extends RecyclerView.Adapter<CustomNoteAdapter.ViewHolder> {

    Cursor cursor;
    Context context;

    public CustomNoteAdapter(Context context, Cursor cursor) {
        this.context = context;
        this.cursor = cursor;
    }
    @NonNull
    @Override
    public CustomNoteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomNoteAdapter.ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        String text = cursor.getString(cursor.getColumnIndex(DataCollectionClass.Collection.COLUMN_NAME_TITLE));
        String desc = cursor.getString(cursor.getColumnIndex(DataCollectionClass.Collection.COLUMN_NAME_DESC));
        String time = cursor.getString(cursor.getColumnIndex(DataCollectionClass.Collection.TIME));
        final Long id = cursor.getLong(cursor.getColumnIndex(DataCollectionClass.Collection._ID));

        holder.title_textView.setText(text);
        holder.desc_textView.setText(desc);
        holder.timeView.setText(time);
        holder.itemView.setTag(id);

        holder.editNoteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NoteInfoActivity.class);
                intent.putExtra("title", holder.title_textView.getText().toString());
                intent.putExtra("description", holder.desc_textView.getText().toString());
                intent.putExtra("time", holder.timeView.getText().toString());
                intent.putExtra("id", String.valueOf(id));

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_textView;
        TextView desc_textView;
        TextView timeView;
        ImageView editNoteView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title_textView = itemView.findViewById(R.id.title_textView);
            desc_textView = itemView.findViewById(R.id.desc_textView);
            timeView = itemView.findViewById(R.id.timeView);
            editNoteView = itemView.findViewById(R.id.editNote);
        }
    }
    public void swapCursor (Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
