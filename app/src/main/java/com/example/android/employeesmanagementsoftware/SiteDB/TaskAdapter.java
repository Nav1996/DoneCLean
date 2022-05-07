package com.example.android.employeesmanagementsoftware.SiteDB;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.taskDB.Task;

import java.util.ArrayList;


public class TaskAdapter extends ArrayAdapter<Task> {
    private Context mContext;
    private ArrayList<Task> tasklist;

    public TaskAdapter(Context context, ArrayList<Task> tasks){
        super(context,0,tasks);
        this.mContext = context;
        this.tasklist = tasks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listview = convertView;

        if(listview == null) {
            listview =  LayoutInflater.from(mContext).inflate(R.layout.task, parent, false);
        }

        Task current = tasklist.get(position);

        TextView name = (TextView) listview.findViewById(R.id.taskName);
        name.setText(current.getTaskName());
        RatingBar ratingBar = (RatingBar) listview.findViewById(R.id.ratingBar_task);
        ratingBar.setRating(current.getEvaluation());

        return listview;
    }

//    @Override
//    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
//        return  LayoutInflater.from(context).inflate(R.layout.task, viewGroup, false);
//    }
//
//    @Override
//    public void bindView(View view, Context context, Cursor cursor) {
//        TextView name = (TextView)view.findViewById(R.id.taskName);
//        name.setText(cursor.getString(cursor.getColumnIndex("TaskName")));
//        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.ratingBar_task);
//        ratingBar.setRating(cursor.getInt(cursor.getColumnIndex("Evaluation")));
//    }
}
