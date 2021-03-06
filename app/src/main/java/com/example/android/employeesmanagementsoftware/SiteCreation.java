package com.example.android.employeesmanagementsoftware;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.employeesmanagementsoftware.SiteDB.DepFragment;
import com.example.android.employeesmanagementsoftware.SiteDB.SiteActivity;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.data.Contracts.SiteContract.DepartmentEntry;
import com.example.android.employeesmanagementsoftware.SiteDB.SiteRowData.SiteItem;
import com.example.android.employeesmanagementsoftware.data.Models.Site;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SiteCreation extends AppCompatActivity {
    private EditText description;
    private EditText nameOfDepartment;
    private EmployeesManagementDbHelper emdb ;
    private DepFragment depFragment = DepFragment.newInstance(0);
    private Button save;
    private Intent intent;
    private long departmentId;
    private Site site;
    DatabaseReference reff;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_creation);
        intent = getIntent();
        boolean IsEditable = intent.getExtras().getBoolean("IsEdit");
        emdb =  new EmployeesManagementDbHelper(this);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        description= findViewById(R.id.department_description);
        nameOfDepartment= findViewById(R.id.department_name);
        firestore = FirebaseFirestore.getInstance();
        reff = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("site");
        save= findViewById(R.id.save);
        if (IsEditable) {
            updateAction();
        } else {
            AddNewDepartemnt();
        }
    }
    private void AddNewDepartemnt(){
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) ||!((description.getText().toString()).matches("^[\\s\\S]{2,200}$")))
                    Snackbar.make(v, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
                else{
                    boolean flag =   emdb.addDepartment( nameOfDepartment.getText().toString(),description.getText().toString());
                    actionSave(flag, v, false);

                    reff.child(nameOfDepartment.getText().toString()).setValue(site = new Site("1001",nameOfDepartment.getText().toString(),description.getText().toString()));
                }
            }
        });

    }

    private void updateAction() {
        departmentId = intent.getExtras().getLong("depatmentID");
        Cursor cursorDep = emdb.getDepartment(departmentId);
        Log.v("Dep cre cur" , ""+departmentId);
        if (cursorDep.moveToFirst()) {
            description.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION)));
            nameOfDepartment.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_NAME)));
        }
        cursorDep.close();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) || !((description.getText().toString()).matches("^[\\s\\S]{2,200}$"))) {
                    Snackbar.make(v, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
                } else {
                    boolean correct = emdb.updateDepartment(new SiteItem(departmentId,nameOfDepartment.getText().toString(),description.getText().toString()));
                    actionSave(correct, v, true);

                }

            }
        });

    }
    private  void actionSave(boolean flag,View v, boolean isEdit) {
        if(flag){
            if(!isEdit)
            Snackbar.make(v, "ENTERED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
            else
                Snackbar.make(v, "UPDATED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
            description.setText("",TextView.BufferType.EDITABLE);
            nameOfDepartment.setText("",TextView.BufferType.EDITABLE);
            depFragment.updateDepartmentList(emdb);
            Intent intent2 = new Intent(getBaseContext(), SiteActivity.class);
            intent2.putExtra("departmentId", departmentId);
            this.finish();
            startActivity(intent2);
        }
        else
            Snackbar.make(v, "FAILED TO ENTER CURRENT DEPARTMENT. TRY AGAIN LATER.", Snackbar.LENGTH_LONG).setAction("", null).show();
    }
}



