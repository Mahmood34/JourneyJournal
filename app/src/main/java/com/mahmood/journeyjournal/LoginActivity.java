package com.mahmood.journeyjournal;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth mAuth;

    EditText usernameEditText;
    EditText passwordEditText;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        findViewById(R.id.sign_up_button).setOnClickListener(this);
        findViewById(R.id.login_button).setOnClickListener(this);

        usernameEditText = findViewById(R.id.username_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        progressBar = findViewById(R.id.log_in_loading);

        mAuth = FirebaseAuth.getInstance();

//        Button btn = (Button)findViewById(R.id.login_button);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });


    }

    private void userLogin(){

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(username.isEmpty()){
            usernameEditText.setError("Username is required");
            usernameEditText.requestFocus();
            return;
        }

//        if(Patterns.EMAIL_ADDRESS.matcher(username).matches()){
//            usernameEditText.setError("Enter a valid email");
//            usernameEditText.requestFocus();
//            return;
//        }

        if(password.isEmpty()){
            passwordEditText.setError("Password is required");
            passwordEditText.requestFocus();
            return;
        }

        if(password.length() < 6){
            passwordEditText.setError("Minimum password length is 6");
            passwordEditText.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);

                if(task.isSuccessful()){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up_button:

                startActivity(new Intent(this, SignupActivity.class));

                break;

            case R.id.login_button:
                userLogin();
                break;
        }
    }
}
