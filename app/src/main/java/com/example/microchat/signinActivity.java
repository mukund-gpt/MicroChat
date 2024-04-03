package com.example.microchat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.microchat.databinding.ActivitySigninBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class signinActivity extends AppCompatActivity {
    ActivitySigninBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).hide();
        

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(signinActivity.this);
        progressDialog.setTitle("Log In");
        progressDialog.setMessage("Login to your Account");

        //configure google signIn
        GoogleSignInOptions gso= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);





        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.email.getText().toString();
                String password = binding.password.getText().toString();

                // Check if email or password is empty
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(signinActivity.this, "Email or password cannot be empty", Toast.LENGTH_SHORT).show();
                    return; // Exit onClick method if email or password is empty
                }

                progressDialog.show();
                auth.signInWithEmailAndPassword(email,password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Intent intent=new Intent(signinActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    if (task.getException() != null) {
                                        Toast.makeText(signinActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(signinActivity.this, "User creation failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
        binding.clickSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signinActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });
        if(auth.getCurrentUser()!=null){
            Intent intent=new Intent(signinActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }
}