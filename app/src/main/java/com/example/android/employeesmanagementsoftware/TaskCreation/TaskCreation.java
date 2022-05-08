package com.example.android.employeesmanagementsoftware.TaskCreation;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.data.Models.TaskClass;
import com.example.android.employeesmanagementsoftware.taskDB.Task;
import com.example.android.employeesmanagementsoftware.taskDB.TaskActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class TaskCreation extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "spinner";
    private Set<Long> employees;
    private TaskCreationCommand commander;
    private TaskCreationUtil util;
    private final EmployeesManagementDbHelper employeeDBHelper= new EmployeesManagementDbHelper(this);
    private Task task;
    public TaskCreation() {
        employees = new TreeSet<>();

    }

    DatabaseReference dbref;
    boolean isEdit;
    Spinner spinner;
    private EditText name, date, description;
    ListView employeeslist, sitelist;
    String dept, taskID;
    TaskClass taskobj;
    ArrayList<String> departments;
    String selectedCleaners;
    GetEmployeeList cleans = new GetEmployeeList();

    ArrayList<String> cleaners = new ArrayList<>();
    String selectSites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation);

        departments = new ArrayList<>();
        setDepartmentsToArray();

        Intent intent = getIntent();
        isEdit = intent.getBooleanExtra("IsEdit", false);
        taskID = intent.getStringExtra("task_id");

//        cleaners = cleans.getEmployees();
        getCleaners();
        Toast.makeText(getApplicationContext(), "Hello " + cleaners, Toast.LENGTH_SHORT).show();

        Bundle taskData=getIntent().getExtras();
        long task_id=TaskCreationUtil.NEW_TASK_ID;
        if (taskData!=null) {
//            task_id = taskData.getString("task_id");
            task = (Task) taskData.get("task");
        }

        name = findViewById(R.id.task_name_edit);
        date = findViewById(R.id.task_deadline_edit);
        description = findViewById(R.id.department_description_edit_text);
        sitelist = findViewById(R.id.departmentlist);
        employeeslist = findViewById(R.id.employees_List);

    }

    private void getSite() {
        sitelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectSites = departments.get(i);
                Toast.makeText(getApplicationContext(), "Site"+selectSites, Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setListview(ArrayList cleaners) {
        ArrayAdapter<String> adapterClean = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1, cleaners);
        employeeslist.setAdapter(adapterClean);

        getEmployees();
    }

    private void getEmployees() {
        employeeslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "Cleaner " +cleaners.get(i), Toast.LENGTH_SHORT).show();
                try {
                    selectedCleaners = cleaners.get(i);
                }
                catch (Exception e) {

                }
            }
        });
    }

    public void setDepartmentsToArray() {
        dbref = FirebaseDatabase.getInstance().getReference().child("site");
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                        Site site = dataSnapshot1.getValue(Site.class);
//                        departments.add(site.getName());
                        try {
                            String nam = dataSnapshot1.child("name").getValue().toString();
//                            Toast.makeText(getApplicationContext(), "Hello " + nam, Toast.LENGTH_SHORT).show();
                            departments.add(nam);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, departments);

                    sitelist.setAdapter(adapter);

                    getSite();
                }
                else {
                    Toast.makeText(getApplicationContext(), "ERRORR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    public void getCleaners() {
//        ArrayList<String> cleaner = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("member").orderByChild("userType").equalTo("Cleaner");
//        dbref = FirebaseDatabase.getInstance().getReference().child("member").child("name")
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            String nam = dataSnapshot1.child("name").getValue().toString();
                            cleaners.add(nam);


                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Erross", Toast.LENGTH_SHORT).show();
                        }
                    }

                    setListview(cleaners);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Errrsss", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    //method to inflate the view of the save button in the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_task_creation, menu);
        return true;
    }

    //method to handle the save button click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //get references to all text layouts wrapping the edit texts
        TextInputLayout deadlineLayout=findViewById(R.id.task_deadline_text_layout);
        TextInputLayout descriptionLayout=findViewById(R.id.task_description_text_layout);
        TextInputLayout nameLayout=findViewById(R.id.task_name_text_layout);

        String name = nameLayout.getEditText().getText().toString();
        String description = descriptionLayout.getEditText().getText().toString();
        String deadline = deadlineLayout.getEditText().getText().toString();

        String rand_id = UUID.randomUUID().toString();



        if (item.getItemId() == R.id.save_task_creation_button) {
            if (isEdit) {
                taskobj = new TaskClass(taskID, name, description, deadline, selectedCleaners, selectSites, false, 0);
                dbref = FirebaseDatabase.getInstance().getReference().child("Tasks").child(taskID);

                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        dbref.setValue(taskobj);
                        Toast.makeText(getApplicationContext(), "Task Edited successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TaskCreation.this, TaskActivity.class);
                        intent.putExtra("task_id", taskID);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
            }
            else {
                taskobj = new TaskClass(rand_id, name, description, deadline, selectedCleaners, selectSites, false, 0);
                dbref = FirebaseDatabase.getInstance().getReference().child("Tasks");

                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        dbref.child(rand_id).setValue(taskobj);
                        Toast.makeText(getApplicationContext(), "Task created successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TaskCreation.this, TaskActivity.class);
                        intent.putExtra("task_id", rand_id);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
            }
        }

        return true;


    }


    public void onDeadlinePick(View view){
        final Calendar mCalendar=Calendar.getInstance();
        final EditText editText=(EditText) view;
        new DatePickerDialog(TaskCreation.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, month);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "dd/MM/yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                editText.setText(sdf.format(mCalendar.getTime()));
            }
        }, mCalendar.get(Calendar.YEAR)
                , mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getApplicationContext(), "Sited", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(getApplicationContext(), "Not Sited", Toast.LENGTH_SHORT).show();
    }
}
