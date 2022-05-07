package com.example.android.employeesmanagementsoftware.SiteDB;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.SiteCreation;
import com.example.android.employeesmanagementsoftware.CleanerCreation;
import com.example.android.employeesmanagementsoftware.CleanerDB.CleanerActivity;
import com.example.android.employeesmanagementsoftware.CleanerDB.CleanerAdapter;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Contracts.SiteContract.DepartmentEntry;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

//need to attach her job with database
// convert actvity to fregment

// /notes and performance of each employee,description edittext,Notify adapter to change
public class SiteActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private final int EMP_REQUEST = 1;
    private EmployeesManagementDbHelper helper;
    private TextView description;
    private ListView employees;
    private CleanerAdapter adapterEmp;
    private long departmentId;
    private String deptId;
    private String depName;
    private DepFragment depFragment = DepFragment.newInstance(0);
    DatabaseReference dbref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.site);
//        helper = new EmployeesManagementDbHelper(this);
        setDepatementParameter();
        setEmployeeList();
        Intent intent = getIntent();
        deptId = intent.getStringExtra("departmentId");
        employees.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SiteActivity.this, CleanerActivity.class);
                intent.putExtra("employeeId", id);
                startActivityForResult(intent, EMP_REQUEST);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SiteActivity.this, CleanerCreation.class);
                intent.putExtra("departmentId", deptId);
                startActivityForResult(intent, EMP_REQUEST);
            }
        });

//        displayTaskList();

    }

    private void setDepatementParameter() {
        description =  findViewById(R.id.description);
        Intent intent = getIntent();
        String departmentId = intent.getStringExtra("departmentId");

        //setting name,description of department

        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("site").child(departmentId);
        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                description.setText(dataSnapshot.child("description").getValue().toString());
                depName = dataSnapshot.child("name").getValue().toString();
                setTitle(depName);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

//        Cursor cursorDep = helper.getDepartment(departmentId);
//        if (cursorDep.moveToFirst()) {
//            description.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION)));
//            depName = cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_NAME));
//            setTitle(depName);
//        }
//        cursorDep.close();
    }


    private void setEmployeeList() {
        //setting list of employees in this department
        employees = findViewById(R.id.cleaner_tasks_list);

        Query query = FirebaseDatabase.getInstance().getReference().child("site").child(deptId);
//        Cursor cursorEmp = helper.getEmployessOfDepartment(departmentId);
//
//        adapterEmp = new CleanerAdapter(this, cursorEmp);
//        employees.setAdapter(adapterEmp);

        RelativeLayout emptyView = (RelativeLayout) findViewById(R.id.empty_employees);
        employees.setEmptyView(emptyView);
    }

//    private void displayTaskList() {
//        Cursor cursorTask = helper.getTasksOfDepartment(departmentId);
//        ListView tasksList = findViewById(R.id.tasks_list);
//        TaskAdapter adapterTask = new TaskAdapter(this, cursorTask);
//        tasksList.setAdapter(adapterTask);
//
//        //RelativeLayout emptyTasks = (RelativeLayout) findViewById(R.id.empty_tasks_dep);
//        //tasksList.setEmptyView(emptyTasks);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == EMP_REQUEST) {
//            if (resultCode == Activity.RESULT_OK) {
//                Cursor cursor = helper.getEmployessOfDepartment(departmentId);
//                adapterEmp.swapCursor(cursor);
//                adapterEmp.notifyDataSetChanged();
//            }
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_site, menu);
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
            Intent intent = new Intent(SiteActivity.this, SiteCreation.class);
            intent.putExtra("depatmentID", deptId);
            intent.putExtra("IsEdit", true);
            startActivity(intent);
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteDep);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbref = FirebaseDatabase.getInstance().getReference().child("site").child(deptId);
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        dbref.removeValue();
                        Toast.makeText(getApplicationContext(), "Deleted sie successfully " + deptId, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Could not delete site", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
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