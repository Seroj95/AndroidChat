package com.example.awesomechat;
// Firbasi het xntirner kan RealmTime injxor chi ashxati miat bti nayvi

// 158 miat nayel etexel xndir ka Name chy grelov kbace:
// sxal kexni Toast kashxati bayc kmtni paymanery Aravot kaux tarm glxov nayel
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG="SignInActivity";

private FirebaseAuth auth;
private EditText emailEditText;
private EditText passwordEditText;
private EditText repeadPasswordEditText;
private EditText nameEditText;
private TextView toggleLogSignUpTextView;
private Button loginSignUpButton;
private  boolean loginModeActivite;
    FirebaseDatabase database;
    DatabaseReference usersDatabaseReferance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        auth = FirebaseAuth.getInstance();

        database=FirebaseDatabase.getInstance();
        usersDatabaseReferance=database.getReference().child("users");

emailEditText=findViewById(R.id.emailEditText);
        passwordEditText=findViewById(R.id.passwordEditText);
        repeadPasswordEditText=findViewById(R.id.repeadPasswordEditText);
        nameEditText=findViewById(R.id.nameEditText);
        toggleLogSignUpTextView=findViewById(R.id.toggleLogSignUpTextView);
        loginSignUpButton=findViewById(R.id.loginSignUpButton);
        loginSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emailEditText.getText().toString().trim(),passwordEditText.getText().toString().trim());
            }
        });
        if (auth.getCurrentUser() !=null){
            startActivity(new Intent(SignInActivity.this,UserListActivity.class));
        }
    }

    private void loginSignUpUser(String email,String password) {
        if (loginModeActivite) {


            if (passwordEditText.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "Password must be at laest 7 characters ", Toast.LENGTH_SHORT).show();

            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please Input your email ", Toast.LENGTH_SHORT).show();

            } else {

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
//                                updateUI(user);
                                    Intent intent =new Intent(SignInActivity.this,UserListActivity.class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                                updateUI(null);
                                }

                                // ...
                            }
                        });


                //hnaravore jnjnvi
//        auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
//                            FirebaseUser user = auth.getCurrentUser();
////                                updateUI(user);
//                            startActivity(new Intent(SignInActivity.this,MainActivity.class));
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
//                            Toast.makeText(SignInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
////                                updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//

                //chi ashhxati

        }


        }else {
            if (!passwordEditText.getText().toString().trim().equals(repeadPasswordEditText.getText().toString().trim())) {
                Toast.makeText(this, "Password dont match", Toast.LENGTH_SHORT).show();

            }else if (passwordEditText.getText().toString().trim().length()<7){
                Toast.makeText(this, "Password must be at laest 7 characters ", Toast.LENGTH_SHORT).show();

            }else if (emailEditText.getText().toString().trim().equals("")){
                Toast.makeText(this, "Please Input your email ", Toast.LENGTH_SHORT).show();

            } else {

                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    createUser(user);
                                    Intent intent =new Intent(SignInActivity.this,UserListActivity.class);
                                    intent.putExtra("userName",nameEditText.getText().toString().trim());
                                    startActivity(intent);
//                            updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                                }

                                // ...
                            }
                        });
            }

        }

        startActivity(new Intent(SignInActivity.this,MainActivity.class));
    }

    private void createUser(FirebaseUser firbaseUser) {
        User user =new User();
        user.setId(firbaseUser.getUid());
        user.setEmail(firbaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());
        usersDatabaseReferance.push().setValue(user);
    }

    public void toggleLoginMode(View view) {
        if (loginModeActivite){
            loginModeActivite=false;
            loginSignUpButton.setText("Sign Up");
            toggleLogSignUpTextView.setText("Or,log in");
            repeadPasswordEditText.setVisibility(View.VISIBLE);
        }else {
            loginModeActivite=true;
            loginSignUpButton.setText("Log in");
            toggleLogSignUpTextView.setText("Or,sign uo");
            repeadPasswordEditText.setVisibility(View.GONE);
        }
    }

}
