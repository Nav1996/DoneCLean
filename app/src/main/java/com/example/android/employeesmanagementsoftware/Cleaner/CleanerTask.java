package com.example.android.employeesmanagementsoftware.Cleaner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.StartingPageActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

public class CleanerTask extends AppCompatActivity {
    TextView taskname;
    Button addBegin, addEnd, setDone;
    String taskId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_task);

        taskname = findViewById(R.id.cleanertaskname);
        addBegin = findViewById(R.id.addBegin);
        addEnd = findViewById(R.id.addEnd);
        setDone = findViewById(R.id.setDone);

        Intent intent = getIntent();
        taskId = intent.getStringExtra("Task_ID");

        getTask(taskId);

        addBegin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleanerTask.this, AddPictures.class);
                intent.putExtra("Task_ID", taskId);
                startActivity(intent);
            }
        });

        addEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CleanerTask.this, AddPictures.class);
                intent.putExtra("Task_ID", taskId);
                startActivity(intent);
            }
        });

    }

    private void getTask(String taskId) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Tasks").child(taskId);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    taskname.setText(dataSnapshot.child("name").getValue().toString());

                    setDone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DatabaseReference dref = dataSnapshot.child("done").getRef();
                            dref.setValue(true);

                            dataSnapshot.getRef().removeValue();
                            Toast.makeText(getApplicationContext(), "Task removed successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CleanerTask.this, StartingPageActivity.class);
                            startActivity(intent);
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }
}