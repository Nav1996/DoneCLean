package com.example.android.employeesmanagementsoftware.SiteDB;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.employeesmanagementsoftware.R;


public class TaskAdapter extends CursorAdapter {

    public TaskAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return  LayoutInflater.from(context).inflate(R.layout.task, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView)view.findViewById(R.id.taskName);
        name.setText(cursor.getString(cursor.getColumnIndex("TaskName")));
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar_task);
        ratingBar.setRating(cursor.getInt(cursor.getColumnIndex("Evaluation")));
    }
}
