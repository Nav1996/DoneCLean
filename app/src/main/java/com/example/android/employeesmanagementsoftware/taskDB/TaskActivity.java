package com.example.android.employeesmanagementsoftware.taskDB;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.CleanerDB.CleanerActivity;
import com.example.android.employeesmanagementsoftware.CleanerDB.CleanerAdapter;
import com.example.android.employeesmanagementsoftware.StartingPageActivity;
import com.example.android.employeesmanagementsoftware.TaskCreation.TaskCreation;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.data.Models.TaskClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.util.ArrayList;


//First you need to show departement and  the employees in this departement who work  in the task
public class TaskActivity extends AppCompatActivity implements Evaluation.EvaluationListner{
    private EmployeesManagementDbHelper employeeDBHelper;
    private TextView datetext;
    private TextView descriptiontext;
    private TextView deadlinetext ;
    private TasksFragment tasksFragment = TasksFragment.newInstance();
    private RatingBar mRatingBar;
    private TextView mEvaluation;
    private int position;
    private ArrayList<Task> tasks;
    private long taskID;
    private CleanerAdapter adapter;

    Button checkImages;
    DatabaseReference dbref;
    TaskClass taskobj;
    String task_id, task_name;
    ListView employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
//         employeeDBHelper = new EmployeesManagementDbHelper(this);

        Intent intent = getIntent();
        task_id = intent.getStringExtra("task_id");
        task_name = intent.getStringExtra("task_name");
//        Toast.makeText(getApplicationContext(), "Task - " + task_id, Toast.LENGTH_SHORT).show();

        getTaskDetails(task_id);

        datetext = findViewById(R.id.taskdate);
        descriptiontext = findViewById(R.id.taskdesc);
        deadlinetext = findViewById(R.id.deadline);
        mRatingBar = findViewById(R.id.ratingBar_task);
        mEvaluation = findViewById(R.id.evaluation);
        employees = findViewById(R.id.cleaner_tasks_list);
        checkImages = findViewById(R.id.checkImages);

//         Intent intent= getIntent();
//         position = intent.getExtras().getInt("position");
//         tasks = (ArrayList<Task>) getIntent().getSerializableExtra("data");
//         taskID = getIntent().getExtras().getLong("taskId");

//
        taskobj = new TaskClass();
        ArrayList<String> cleaners = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        String currentDateandTime = sdf.format(new Date());

        if(task_name == null) {

            dbref = FirebaseDatabase.getInstance().getReference().child("Tasks").child(task_id);

            dbref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    taskobj.setName(dataSnapshot.child("name").getValue().toString());
                    taskobj.setDeadline(dataSnapshot.child("deadline").getValue().toString());
                    taskobj.setDescription(dataSnapshot.child("description").getValue().toString());
                    taskobj.setId(dataSnapshot.child("id").getValue().toString());
                    taskobj.setDone((boolean) dataSnapshot.child("done").getValue());
    //                taskobj.setEvaluation(dataSnapshot.child("evaluation").getValue());
                    taskobj.setEmployees(dataSnapshot.child("employees").getValue().toString());

                    datetext.setText(currentDateandTime);
                    descriptiontext.setText(taskobj.getDescription());
                    deadlinetext.setText(taskobj.getDeadline());
                    cleaners.add(taskobj.getEmployees());
                    if (taskobj.isDone()) {
                        mRatingBar.setRating(taskobj.getEvaluation());
                        mRatingBar.setVisibility(View.VISIBLE);
                        mEvaluation.setVisibility(View.VISIBLE);
                    }

                    ArrayAdapter<String> adapterClean = new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1, cleaners);
                    employees.setAdapter(adapterClean);
                    onEmployeeClicked();
                    checkTaskImages();
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                }
            });
        }
        else {
            Query query = FirebaseDatabase.getInstance().getReference().child("Tasks").orderByChild("name").equalTo(task_name);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            task_id = dataSnapshot.child("id").getValue().toString();
                            taskobj.setName(dataSnapshot.child("name").getValue().toString());
                            taskobj.setDeadline(dataSnapshot.child("deadline").getValue().toString());
                            taskobj.setDescription(dataSnapshot.child("description").getValue().toString());
                            taskobj.setId(dataSnapshot.child("id").getValue().toString());
                            taskobj.setDone((boolean) dataSnapshot.child("done").getValue());
                            //                taskobj.setEvaluation(dataSnapshot.child("evaluation").getValue());
                            taskobj.setEmployees(dataSnapshot.child("employees").getValue().toString());

                            datetext.setText(currentDateandTime);
                            descriptiontext.setText(taskobj.getDescription());
                            deadlinetext.setText(taskobj.getDeadline());
                            cleaners.add(taskobj.getEmployees());
                            if (taskobj.isDone()) {
                                mRatingBar.setRating(taskobj.getEvaluation());
                                mRatingBar.setVisibility(View.VISIBLE);
                                mEvaluation.setVisibility(View.VISIBLE);
                            }

                            ArrayAdapter<String> adapterClean = new ArrayAdapter<>(getApplicationContext(),
                                    android.R.layout.simple_list_item_1, cleaners);
                            employees.setAdapter(adapterClean);
                            onEmployeeClicked();
                            checkTaskImages();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                }
            });
        }

//        setEmployees();


    }

    public void checkTaskImages() {
        checkImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TaskActivity.this, TaskCheckImages.class);
                intent1.putExtra("TaskID", task_id);
                startActivity(intent1);
            }
        });
    }

    private void getTaskDetails(String task_id) {

    }

    private void getEmployee(String name) {
//        dbref = FirebaseDatabase.getInstance().getReference().child("member")
    }

    private void onEmployeeClicked() {
        employees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TaskActivity.this, CleanerActivity.class);
                intent.putExtra("Emp_ID", taskobj.getEmployees());
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
            showDeleteConfirmationDialog();

        }
        if (id == R.id.action_update) {
            Intent intent = new Intent(TaskActivity.this, TaskCreation.class);
            intent.putExtra("task", taskobj.getClass());
            intent.putExtra("task_id",taskobj.getId());
            intent.putExtra("IsEdit", true);
            finish();
            startActivity(intent);
        }
        if (id == R.id.action_done) {
            openDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    public void openDialog(){
        Evaluation evaluation = new Evaluation();
        evaluation.show(getSupportFragmentManager(),"EvaluationTaskDialog");
    }


    @Override
    public void applyingRating(int rate) {
//        Log.v("ID From Activityr", "" + taskobj.getId());
//        boolean re = employeeDBHelper.updateTaskEvaluation(tasks.get(position).getId(),true,rate);
//        Log.v("boolean", "" + re);
        taskobj.setEvaluation(rate);
        taskobj.setDone(true);
//        TasksFragment.newInstance().updateTasksList(tasks.get(position),(int)taskID);
        mRatingBar.setRating(rate);
        mRatingBar.setVisibility(View.VISIBLE);
        mEvaluation.setVisibility(View.VISIBLE);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this task ?");
        builder.setPositiveButton("End", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Tasks");
                DatabaseReference delRef;

                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(task_id)) {
                            DatabaseReference delRef = FirebaseDatabase.getInstance().getReference().child("Tasks").child(task_id);
                            delRef.removeValue();
                            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(TaskActivity.this, StartingPageActivity.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
