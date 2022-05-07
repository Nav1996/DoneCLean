package com.example.android.employeesmanagementsoftware.login;

import android.content.Intent;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.R;
import com.example.android.employeesmanagementsoftware.data.Models.Member;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    DatabaseReference reff;
    FirebaseFirestore firestore;
    FirebaseAuth firebaseAuth;
    Member member;
    Spinner user;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,15}" +
                    "$");
    private static final Pattern EMAIL= Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
    private TextInputEditText fullname;
    private TextInputEditText email;
    private TextInputEditText mobile;
    private TextInputEditText location;
    private TextInputEditText pswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fullname = findViewById(R.id.name);
        email = findViewById(R.id.email);
        mobile = findViewById(R.id.mobile);
        location = findViewById(R.id.location);
        pswd = findViewById(R.id.password);
        user = findViewById(R.id.usertype);
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_spinner_item);
        user.setAdapter(adapter);
        user.setOnItemSelectedListener(this);
        member = new Member();
        firestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        reff = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("member");
    }
    private void addUser(Member member, String uid)
    {
        reff.child(uid).setValue(member);
//        firestore.collection("member")
//                .document(uid).set(member);
    }

    private boolean validefullname()
    {
        String full=fullname.getText().toString().trim();

        if(full.isEmpty())
        {
            fullname.setError("Fullname can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validelocation()
    {
        String place=location.getText().toString().trim();
        if(place.isEmpty())
        {
            location.setError("location can't be empty");
            return false;
        }
        return  true;
    }
    private boolean validemobile()
    {
        String mob=mobile.getText().toString().trim();
        if(mob.isEmpty())
        {
            mobile.setError("mobile number can't be empty");
            return false;
        }
        return  true;
    }

    private boolean valideEmail() {
        String emailinput = email.getText().toString().trim();
        if (emailinput.isEmpty()) {
            email.setError("Email can't be empty");
            return false;
        } else if (!EMAIL.matcher(emailinput).matches()) {
            email.setError("Please enter a valid email address");
            return false;
        } else {
            email.setError(null);
            return true;
        }
    }

    private boolean validePassword() {
        String passinput = pswd.getText().toString().trim();
        if (passinput.isEmpty()) {
            pswd.setError("password can't be empty");
            return false;
        } else if (!PASSWORD_PATTERN.matcher(passinput).matches()) {
            pswd.setError("Password too weak");
            return false;
        } else {
            pswd.setError(null);
            return true;
        }
    }

    public void signin(View v) {
        member.setName(fullname.getText().toString().trim());
        member.setEmail(email.getText().toString().trim());
        try {
            member.setPhone(mobile.getText().toString().trim());
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
        member.setLocation(location.getText().toString().trim());
        member.setPassword(pswd.getText().toString().trim());

        if (!valideEmail() | !validePassword()  |!validefullname() |!validelocation() |!validemobile()) {
            return;
        }
        String input = "Email: " + email.getText().toString();
        input += "\n";
        firebaseAuth
                .createUserWithEmailAndPassword(email.getText().toString().trim(),pswd.getText().toString().trim())
                .addOnFailureListener(
                        task -> {
                            System.out.println(task.getCause());
                        }
                )
                .addOnCompleteListener(
                        task -> {
                            if(task.isSuccessful())
                            {
                                String uid = task.getResult().getUser().getUid();
                                addUser(member,uid);
                                Toast.makeText(RegisterActivity.this, "Data input & user created successfully", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                            else if(!task.isSuccessful())
                            {
                                System.out.println(task.getException());
                                Toast.makeText(RegisterActivity.this,"Email id already exists",Toast.LENGTH_SHORT).show();
                            }
                        }
                );
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            member.setUserType("Supervisor");
            Toast.makeText(getApplicationContext(),"Supervisor",Toast.LENGTH_SHORT).show();
        }
        else
        {
            member.setUserType("Cleaner");
            Toast.makeText(getApplicationContext(),"Cleaner",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}