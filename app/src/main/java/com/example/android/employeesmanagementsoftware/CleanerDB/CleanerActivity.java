package com.example.android.employeesmanagementsoftware.CleanerDB;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.Utilities.CustomEditTextWithBullets;
import com.example.android.employeesmanagementsoftware.data.Contracts.CleanerContract.EmployeeEntry;
import com.example.android.employeesmanagementsoftware.data.DBHelpers.EmployeesManagementDbHelper;
import com.example.android.employeesmanagementsoftware.data.Models.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class CleanerActivity extends AppCompatActivity {
    private static final int PICK_FROM_GALLERY = 1;
    private static int RESULT_LOAD_IMAGE = 1;
    private EmployeesManagementDbHelper helper;
    private EditText name, email, phone, birthday, job;
    private CustomEditTextWithBullets notes;
    private ImageView image;
    private RatingBar performanceRatBar;
    private ListView tasksList;
    private long employeeId;
    private String picturePath;
    private boolean imgChanged = false;
    String emp_id;
    Member cleaner;
    DatabaseReference dbref;
    ArrayList<String> tasks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cleaner);

        Intent intent2 = getIntent();
        emp_id = intent2.getStringExtra("Emp_ID");

        tasksList = findViewById(R.id.tasks_list);
        name = findViewById(R.id.employee_name);
        email = findViewById(R.id.email);
        phone =  findViewById(R.id.phone);
        birthday = findViewById(R.id.birthday);
        job = findViewById(R.id.post);
        notes = findViewById(R.id.notes);
        image = findViewById(R.id.employee_icon);
        image.setTag("");
        picturePath = new String();
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (ActivityCompat.checkSelfPermission(CleanerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CleanerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


//        helper = new EmployeesManagementDbHelper(this);
//        Intent intent = getIntent();
//        employeeId = intent.getExtras().getLong("employeeId");
        setEmployee();
//        setEmployeeTasks();
    }

    private void setEmployeeTasks() {
        tasksList = (ListView) findViewById(R.id.tasks_list);
        tasks = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("Tasks").
                orderByChild("employees").equalTo(emp_id);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                tasks.add(dataSnapshot.child("name").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(), "Task - "+ tasks, Toast.LENGTH_SHORT).show();

    }

    private void setPerformance(Cursor cursor) {

        int performance = 0,count = 0,evaluation = 0;

        if (cursor.moveToFirst() && cursor.getCount() > 0) {
           do {
               boolean value = cursor.getInt(cursor.getColumnIndex(getString(R.string.completed))) > 0;
               evaluation = cursor.getInt(cursor.getColumnIndex(getString(R.string.evaluation)));
               if(value ){
                   count++;
                   performance += evaluation;
               }
            }while (cursor.moveToNext());
            float res = (float) performance / count;
            performanceRatBar =  findViewById(R.id.ratingBar_employee);
            performanceRatBar.setRating(res);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_cleaner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar menu
        switch (item.getItemId()) {
            // Respond to a click on the "Edit" menu option
            case R.id.action_save:
                editEmployee();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_GALLERY:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_FROM_GALLERY);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            imgChanged = true;
            image.setTag(picturePath);
        }
    }

    private void showDeleteConfirmationDialog() {
        dbref = FirebaseDatabase.getInstance().getReference().child("member").child(emp_id);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteEmp);
        builder.setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dbref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        dbref.removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Can't fire employee", Toast.LENGTH_SHORT).show();
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

    private void setEmployee() {
//        Cursor cursor = helper.getEmployee(employeeId);
        Query query = FirebaseDatabase.getInstance().getReference().child("member").orderByChild("name").equalTo(emp_id);
        //setting data of employee
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(getApplicationContext(), "Emp -"+dataSnapshot.child("email").getValue(), Toast.LENGTH_SHORT).show();
//                    name.setText(dataSnapshot.get().toString());
//                    email.setText(dataSnapshot.child("email").getValue().toString());
//                    phone.setText(dataSnapshot.child("phone").getValue().toString());
//                    job.setText(dataSnapshot.child("userType").getValue().toString());
//                    name.setText(dataSnapshot.child("name").getValue().toString());
//
//                    cleaner = new Member(dataSnapshot.child("name").getValue().toString(),
//                            dataSnapshot.child("email").getValue().toString(),
//                            dataSnapshot.child("password").getValue().toString(),
//                            dataSnapshot.child("location").getValue().toString(),
//                            dataSnapshot.child("phone").getValue().toString(),
//                            dataSnapshot.child("userType").getValue().toString());
//
//                    setEmployeeTasks();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Else called", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }


    private void editEmployee() {

        ContentValues values = new ContentValues();

        values.put(EmployeeEntry.COLUMN_EMPLOYEE_NAME, name.getText().toString().trim());
        values.put(EmployeeEntry.COLUMN_EMPLOYEE_BIRTHDATE, birthday.getText().toString().trim());
        values.put(EmployeeEntry.COLUMN_EMPLOYEE_JOB, job.getText().toString().trim());
        values.put(EmployeeEntry.COLUMN_EMPLOYEE_EMAIL, email.getText().toString().trim());
        values.put(EmployeeEntry.COLUMN_EMPLOYEE_NOTES, notes.getText().toString().trim());
        values.put(EmployeeEntry.COLUMN_EMPLOYEE_PHONE, phone.getText().toString().trim());
        if (imgChanged) {
            values.put(EmployeeEntry.COLUMN_EMPLOYEE_PHOTO, image.getTag().toString());
        }
        if (helper.updateEmployee(employeeId, values)) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();

        }
    }
}
