package com.example.android.employeesmanagementsoftware.TaskCreation;

import android.os.AsyncTask;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;

public class GetEmployeeList extends AsyncTask<URL, Integer, ArrayList> {
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("member");

    public GetEmployeeList() {
    }

    @Override
    protected ArrayList doInBackground(URL... urls) {
        long arrs = 0;
        ArrayList<String> employees = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("member").orderByChild("userType").equalTo("Cleaner");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        employees.add((String) dataSnapshot1.child("name").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
        return employees;
    }

    protected void onPostExecute() {

    }

    public ArrayList getEmployees() {
        ArrayList<String> employees = new ArrayList<>();
        Query query = FirebaseDatabase.getInstance().getReference().child("member").orderByChild("userType").equalTo("Cleaner");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        employees.add((String) dataSnapshot1.child("name").getValue());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

        if(employees.size() > 0) {
            return employees;
        }
        else {
            return null;
        }
    }
}
