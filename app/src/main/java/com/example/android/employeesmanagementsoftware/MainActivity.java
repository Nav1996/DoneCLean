package com.example.android.employeesmanagementsoftware;

import android.content.Intent;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.Cleaner.CleanerMainPage;
import com.example.android.employeesmanagementsoftware.data.Models.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


// see how to connect 3 main activity ** department ** employies **tasks
public class MainActivity extends AppCompatActivity {
    FirebaseUser user;
    String uid;
    String usertype, name, username, location;
    DatabaseReference databaseReference;
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    private static int SPLASH_TIME_OUT = 750;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                databaseReference = FirebaseDatabase.getInstance().getReference("member");
                user = FirebaseAuth.getInstance().getCurrentUser();
                uid = user.getUid();

                databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        Member current = dataSnapshot.getValue(Member.class);
                        String usertype1 = current.getUserType();

                        Toast.makeText(MainActivity.this, "user : " + usertype1, Toast.LENGTH_SHORT).show();

                        if(usertype1.equals("Cleaner")) {
                            Intent cleaner = new Intent(MainActivity.this, CleanerMainPage.class);
                            startActivity(cleaner);
                            finish();
                        }
                        else {
                            Intent home = new Intent(MainActivity.this,StartingPageActivity.class);
                            startActivity(home);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
            }
        },SPLASH_TIME_OUT);

    }

    public void get_UserType(String uid) {

    }

    public void set_Usertype(String usert) {
        usertype = usert;
    }

}
