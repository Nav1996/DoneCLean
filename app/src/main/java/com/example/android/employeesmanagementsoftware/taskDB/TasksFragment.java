package com.example.android.employeesmanagementsoftware.taskDB;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Contracts.TaskContract.TaskEntry;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class TasksFragment extends Fragment implements SelectTaskListener{
    DatabaseReference dbref;
    private static TasksFragment fragment ;
    private EmployeesManagementDbHelper employeeDBHelper;
    private ArrayList<Task> mValues;
    private TasksAdapter mAdapter;
    @SuppressLint("ValidFragment")
    private TasksFragment() {

    }
    public static TasksFragment newInstance() {
        if (fragment == null){
             fragment = new TasksFragment();
        }
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mValues = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mValues = new ArrayList<>();

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Tasks");

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        try {
                            Task task = new Task();
                            task.setTaskName(snapshot.child("name").getValue().toString());
                            task.setTaskDetails(snapshot.child("description").getValue().toString());
                            task.setTaskDeadline(snapshot.child("deadline").getValue().toString());

                            mValues.add(task);
                        }catch (Exception e) {
                            Toast.makeText(getActivity(), "No data", Toast.LENGTH_SHORT).show();
                        }
                    }

                    RecyclerView recyclerView =  view.findViewById(R.id.task_list);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    mAdapter = new TasksAdapter(getActivity(),mValues, TasksFragment.this);
                    recyclerView.setAdapter(mAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }

    public boolean updateTasksList(Task updatedTask, int id){
        for(int i = 0; i < mValues.size(); i++) {
            if (mValues.get(i).getId() == id) {
                mValues.remove(i);
                mValues.add(i,updatedTask);
                break;
            }
        }
        mAdapter.notifyItemChanged(mValues.indexOf(updatedTask), updatedTask);
        return employeeDBHelper.updateTask(updatedTask);
    }

    public boolean deleteTaskFromList(int id ){
        boolean remove = employeeDBHelper.deleteTask(id);
        for(int i = 0; i < mValues.size(); i++) {
            if (mValues.get(i).getId() == id) {
                mValues.remove(i);
                break;
            }
        }
        mAdapter.notifyDataSetChanged();
        return remove;
    }
    public boolean addTaskToView(Task mTask){
        long id = mTask.getId();
        try {
            String task_id = String.valueOf(id);
            dbref = FirebaseDatabase.getInstance().getReference().child("Tasks").child(task_id);
            try {
                dbref.setValue(mTask);

            } catch (Exception e) {

            }
            //        Long id  = employeeDBHelper.addTask(mTask);
            //        mTask.setId(id.toString());
            mValues.add(mTask);
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }

        }
        catch (Exception e) {
            Toast.makeText(getContext(), "Cannot save task", Toast.LENGTH_SHORT).show();
        }
        return id > 0;
    }


    @Override
    public void onItemClicked(Task task) {
        Toast.makeText(getActivity(), "TaskNAme - "+task.getTaskName(), Toast.LENGTH_SHORT).show();         //

        Intent intent = new Intent(TasksFragment.this.getActivity(), TaskActivity.class);
        intent.putExtra("task_name", task.getTaskName());
        startActivity(intent);
    }
}
