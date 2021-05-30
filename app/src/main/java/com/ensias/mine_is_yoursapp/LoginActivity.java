package com.ensias.mine_is_yoursapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ensias.mine_is_yoursapp.Control.ForgotPassword;
import com.ensias.mine_is_yoursapp.Control.SessionManager;
import com.ensias.mine_is_yoursapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    EditText username, password, reg_username, reg_password,reg_firstName, reg_lastName, reg_email, reg_confirmemail;
    Button login, signUp, reg_register;
    TextInputLayout txtInLayoutUsername, txtInLayoutPassword, txtInLayoutRegPassword;
    CheckBox rememberMe;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        txtInLayoutUsername = findViewById(R.id.txtInLayoutUsername);
        txtInLayoutPassword = findViewById(R.id.txtInLayoutPassword);
        rememberMe = findViewById(R.id.rememberMe);

        SessionManager sessionManager = new SessionManager(LoginActivity.this,SessionManager.SESSION_REMEMBERME);
        if(sessionManager.checkRememberMe()){
            HashMap<String,String> userLogin = sessionManager.getRememberMeSessionDetails();
            username.setText(userLogin.get(SessionManager.KEY_SESSION_EMAIL));
            password.setText(userLogin.get(SessionManager.KEY_SESSION_PASSWORD));
        }


        ClickLogin();


        //SignUp's Button for showing registration page
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClickSignUp();
            }
        });
    }
    //SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
    //String rememberBox = "Hello";//preferences.getString("remember","");
    //boolean isTrue = rememberBox.equals("true");
    //Intent intent = new Intent(LoginActivity.this,MenuPrincipaleActivity.class);
    //startActivity(intent);

    //This is method for doing operation of check login
    private void ClickLogin() {

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (username.getText().toString().trim().isEmpty()) {

                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    txtInLayoutUsername.setError("Username should not be empty");
                } else {
                    //checking username
                }
                if (password.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar.make(view, "Please fill out these fields",
                            Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(getResources().getColor(R.color.red));
                    snackbar.show();
                    txtInLayoutPassword.setError("Password should not be empty");
                } else {
                    //checking password
                }

                if (rememberMe.isChecked()) {
                    SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSION_REMEMBERME);
                    sessionManager.createRememberMeSession(username.getText().toString().trim(), password.getText().toString().trim());
                }
/*
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","true");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Checked",Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences preferences = getSharedPreferences("checkbox",MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember","false");
                    editor.apply();
                    Toast.makeText(LoginActivity.this,"Unchecked",Toast.LENGTH_SHORT).show();
                }
*/
                mAuth.signInWithEmailAndPassword(username.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Welcome!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MenuPrincipaleActivity.class));
                    }else{
                        Toast.makeText(LoginActivity.this,"Something went wrong !"+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                    }
                    }
                });

            }

        });

    }

    //The method for opening the registration page and another processes or checks for registering
    private void ClickSignUp() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.register, null);
        dialog.setView(dialogView);

        reg_username = dialogView.findViewById(R.id.reg_username);
        reg_password = dialogView.findViewById(R.id.reg_password);
        reg_firstName = dialogView.findViewById(R.id.reg_firstName);
        reg_lastName = dialogView.findViewById(R.id.reg_lastName);
        reg_email = dialogView.findViewById(R.id.reg_email);
        reg_confirmemail = dialogView.findViewById(R.id.reg_confirmemail);
        reg_register = dialogView.findViewById(R.id.reg_register);
        txtInLayoutRegPassword = dialogView.findViewById(R.id.txtInLayoutRegPassword);

        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reg_username.getText().toString().trim().isEmpty()) {

                    reg_username.setError("Please fill out this field");
                } else {
                    //Here you can write the codes for checking username
                }
                if (reg_password.getText().toString().trim().isEmpty()) {
                    txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(false);
                    reg_password.setError("Please fill out this field");
                } else {
                    txtInLayoutRegPassword.setPasswordVisibilityToggleEnabled(true);
                    //Here you can write the codes for checking password
                }
                if (reg_firstName.getText().toString().trim().isEmpty()) {

                    reg_firstName.setError("Please fill out this field");
                } else {
                    //Here you can write the codes for checking firstname

                }
                if (reg_lastName.getText().toString().trim().isEmpty()) {

                    reg_lastName.setError("Please fill out this field");
                } else {
                    //Here you can write the codes for checking lastname
                }
                if (reg_email.getText().toString().trim().isEmpty()) {

                    reg_email.setError("Please fill out this field");
                } else {
                    //Here you can write the codes for checking email
                }
                if (reg_confirmemail.getText().toString().trim().isEmpty()) {

                    reg_confirmemail.setError("Please fill out this field");
                } else {
                    //Here you can write the codes for checking confirmemail
                }

                mAuth.createUserWithEmailAndPassword(reg_email.getText().toString().trim(),reg_password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://mineisyours-68d08-default-rtdb.firebaseio.com/");
                            DatabaseReference myRef = database.getReference();
                            Toast.makeText(LoginActivity.this, "User created successufully!",Toast.LENGTH_SHORT).show();
                            User user = new User("2",2d,2d,reg_firstName.getText().toString(),reg_lastName.getText().toString(),reg_email.getText().toString(),"default");
                            myRef.push().setValue(user);
                            startActivity(new Intent(getApplicationContext(),MenuPrincipaleActivity.class));
                            User userSession= new User("4", 0.0, 0.0, reg_firstName.getText().toString().trim(), reg_lastName.getText().toString().trim(), reg_email.getText().toString().trim(), null);
                            SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_USERSESSION);
                            sessionManager.createUserSession(userSession,reg_password.getText().toString().trim());
                        }else{
                            Toast.makeText(LoginActivity.this,"User was not created !"+task.getException().getMessage() ,Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        dialog.show();
        //startActivity(new Intent(getApplicationContext(),DashboardActivity.class));
    }

    public void callForgotPassword(View view){
        startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
    }

}