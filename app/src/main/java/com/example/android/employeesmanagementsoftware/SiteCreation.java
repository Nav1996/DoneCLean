package com.example.android.employeesmanagementsoftware;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.SiteDB.DepFragment;
import com.example.android.employeesmanagementsoftware.SiteDB.SiteActivity;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.data.Models.Site;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SiteCreation extends AppCompatActivity {
    private EditText description;
    private EditText nameOfDepartment;
    private EmployeesManagementDbHelper emdb ;
    private DepFragment depFragment = DepFragment.newInstance(0);
    private Button save;
    private Intent intent;
    private long departmentId;
    private Site site;
    boolean IsEditable;
    DatabaseReference reff, readreff;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_creation);
        intent = getIntent();
        IsEditable = intent.getExtras().getBoolean("IsEdit");
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
            updateAction(reff);
        } else {
            AddNewDepartemnt(reff);
        }
    }
    private void AddNewDepartemnt(DatabaseReference reff){
        save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) ||!((description.getText().toString()).matches("^[\\s\\S]{2,200}$")))
                    Snackbar.make(v, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
                else{
                    String rand_id = UUID.randomUUID().toString();
                    site = new Site(rand_id, nameOfDepartment.getText().toString(), description.getText().toString());

                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                            reff.child(rand_id).setValue(site);
                            Toast.makeText(getApplicationContext(), "Successfuly added Site", Toast.LENGTH_SHORT).show();
                            actionSave(true, v, false, rand_id);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "Cannot save site", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void updateAction(DatabaseReference reff) {
        String dep_id = intent.getStringExtra("depatmentID");

        readreff = reff.child(dep_id);

        readreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                description.setText(dataSnapshot.child("description").getValue().toString());
                nameOfDepartment.setText(dataSnapshot.child("name").getValue().toString());

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) || !((description.getText().toString()).matches("^[\\s\\S]{2,200}$"))) {
                            Snackbar.make(view, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
                        }
                        else {
                            site = new Site(dep_id, nameOfDepartment.getText().toString(), description.getText().toString());
                            readreff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                                    readreff.setValue(site);
                                    Toast.makeText(getApplicationContext(), "Successfuly added Site", Toast.LENGTH_SHORT).show();
                                    actionSave(true, view, false, dep_id);

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                                    Toast.makeText(getApplicationContext(), "Cannot save site", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Cannot save site", Toast.LENGTH_SHORT).show();
            }
        });

//        Cursor cursorDep = emdb.getDepartment(departmentId);
//        Log.v("Dep cre cur" , ""+departmentId);
//        if (cursorDep.moveToFirst()) {
//            description.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION)));
//            nameOfDepartment.setText(cursorDep.getString(cursorDep.getColumnIndex(DepartmentEntry.COLUMN_DEPARTMENT_NAME)));
//        }
//        cursorDep.close();
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!((nameOfDepartment.getText().toString()).matches("^\\w+( \\w+)*$")) || !((description.getText().toString()).matches("^[\\s\\S]{2,200}$"))) {
//                    Snackbar.make(v, "SOME OR ALL INPUTS ARE INVALID. PLEASE ENTER VALID VALUES.", Snackbar.LENGTH_LONG).setAction("", null).show();
//                } else {
//                    boolean correct = emdb.updateDepartment(new SiteItem(departmentId,nameOfDepartment.getText().toString(),description.getText().toString()));
//                    actionSave(correct, v, true);
//
//                }
//
//            }
//        });

    }
    private  void actionSave(boolean flag,View v, boolean isEdit, String dep_id) {
        Intent intent2 = new Intent(getBaseContext(), SiteActivity.class);
        intent2.putExtra("departmentId", dep_id);
        startActivity(intent2);
        finish();
//        if(flag){
//            if(!isEdit)
//            Snackbar.make(v, "ENTERED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
//            else
//                Snackbar.make(v, "UPDATED SUCCESSFULLY", Snackbar.LENGTH_LONG).setAction("", null).show();
//            description.setText("",TextView.BufferType.EDITABLE);
//            nameOfDepartment.setText("",TextView.BufferType.EDITABLE);
//            depFragment.updateDepartmentList(emdb);
//            Intent intent2 = new Intent(getBaseContext(), SiteActivity.class);
//            intent2.putExtra("departmentId", dep_id);
//            this.finish();
//            startActivity(intent2);
//        }
//        else
//            Snackbar.make(v, "FAILED TO ENTER CURRENT DEPARTMENT. TRY AGAIN LATER.", Snackbar.LENGTH_LONG).setAction("", null).show();
    }
}



