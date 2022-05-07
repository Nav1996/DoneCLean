package com.example.android.employeesmanagementsoftware.Cleaner;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Models.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CleanerMainPage extends AppCompatActivity {
    ListView tasksList;
    FirebaseUser user;
    DatabaseReference dbref;
    String uid;
    ArrayList<String> tasks, taskIDs;
    Member current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaner_main_page);
        tasksList = findViewById(R.id.cleaner_tasks_list);

        dbref = FirebaseDatabase.getInstance().getReference("member");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        dbref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                current = dataSnapshot.getValue(Member.class);

                Toast.makeText(getApplicationContext(), "Welcome " +current.getName(), Toast.LENGTH_SHORT).show();

                getTasks();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    private void getTasks() {
        tasks = new ArrayList<>();
        taskIDs = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference("Tasks").orderByChild("employees").equalTo(current.getName());

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    tasks.add(snapshot.child("name").getValue().toString());
                    taskIDs.add(snapshot.child("id").getValue().toString());
                }

                ArrayAdapter<String> adapterClean = new ArrayAdapter<>(getApplicationContext(),
                        android.R.layout.simple_list_item_1, tasks);
                tasksList.setAdapter(adapterClean);
                taskClicked();

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }

    private void taskClicked() {
        tasksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CleanerMainPage.this, AddPictures.class);
                intent.putExtra("Task_ID", taskIDs.get(i));
                startActivity(intent);
            }
        });
    }


}