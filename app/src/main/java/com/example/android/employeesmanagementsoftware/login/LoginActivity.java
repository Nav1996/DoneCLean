package com.example.android.employeesmanagementsoftware.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.google.android.material.textfield.TextInputEditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.android.employeesmanagementsoftware.MainActivity;
import com.example.android.employeesmanagementsoftware.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText email,pass;
    Button login;
    FirebaseAuth firebaseAuth;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    String userEmail,userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.editEmail);
        pass = findViewById(R.id.editPassword);
        login = (Button) findViewById(R.id.userlogin);
        firebaseAuth = FirebaseAuth.getInstance();
        saveLoginCheckBox = (CheckBox)findViewById(R.id.rempasswordcheckbox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            email.setText(loginPreferences.getString("username", ""));
            pass.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }
        login.setOnClickListener(this);
    }
    private boolean valideemail()
    {
        String user = email.getText().toString().trim();
        if(user.isEmpty())
        {
            email.setError("Email can't be empty");
            return false;
        }
        return  true;

    }
    private boolean validepass()
    {
        String passwd=pass.getText().toString().trim();

        if(passwd.isEmpty())
        {
            pass.setError("password can't be empty");
            return false;
        }
        return  true;
    }
    public  void signup(View v)
    {
        Intent i=new Intent(this, RegisterActivity.class);
        startActivity(i);
    }

    public void setValidLogin()
    {
        if(!validepass() | !valideemail()){
            return;
        }
        userEmail = email.getText().toString().trim();
        userPassword = pass.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword(
                userEmail, userPassword)
                .addOnCompleteListener(
                        task -> {
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (pass.length() < 8) {
                                    Toast.makeText(getApplicationContext(),"Password must be more than 8 digit",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(),"Enter the correct email and password",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else if(task.isSuccessful()) {
                                Intent i= new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                            else {
                                System.out.println(email.getText().toString()+pass.getText().toString());
                                Toast.makeText(getApplicationContext(),"Enter the correct email and password",Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    @Override
    public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(email.getWindowToken(), 0);
        userEmail = email.getText().toString();
        userPassword = pass.getText().toString();
        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", userEmail);
            loginPrefsEditor.putString("password", userPassword);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }
        setValidLogin();
    }

}