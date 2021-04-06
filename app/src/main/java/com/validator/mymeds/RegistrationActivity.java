package com.validator.mymeds;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.validator.mymeds.model.Profile;

/**
 * Class sets up activity allowing users to register for a new profile with the application.
 */
public class RegistrationActivity extends AppCompatActivity {
    public static void addProfile(Profile p) {
        DocumentReference ref = FirebaseFirestore.getInstance().collection("profiles").document(p.getAuthId());
        ref.set(p);
    }

    private FirebaseAuth authUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authUser.getInstance().signOut();
        setContentView(R.layout.activity_registration);

        Button registerSubmit = (Button) findViewById(R.id.registerSubmit);
        final EditText registerEmail = (EditText) findViewById(R.id.registerEmail);
        final EditText registerPassword = (EditText) findViewById(R.id.registerPassword);
        final EditText registerName = (EditText) findViewById(R.id.registerName);
        final EditText registerUsername = (EditText) findViewById(R.id.registerUsername);
        final TextView navToMain = (TextView) findViewById(R.id.navToMain);
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        authUser = FirebaseAuth.getInstance();

        //Navigate back to login screen.
        navToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navToLogin = new Intent(RegistrationActivity.this, MainActivity.class);
                startActivity(navToLogin);
            }
        });

        registerSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = registerEmail.getText().toString();
                final String username = registerUsername.getText().toString();
                final String password = registerPassword.getText().toString();
                final String name = registerName.getText().toString();

                RegistrationRules rules = new RegistrationRules(email, password, name);
                if (rules.runRules() != 4) {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Credentials", Toast.LENGTH_SHORT).show();
                }
                else {
                    /* Firebase create authUser method is only called if no rules fail. */
                    authUser.createUserWithEmailAndPassword(email, password)
                            .addOnFailureListener(RegistrationActivity.this, new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    System.out.println(e.getMessage());
                                }
                            })
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {

                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        /* A Profile object is only created if a user successfully
                                        creates an authUser login. The Profile is then stored in the database. */
                                        FirebaseUser user = authUser.getCurrentUser();
                                        Profile p = new Profile(name, username, email, email);
                                        addProfile(p);
                                        finish();
                                    } else {
                                        /* If sign in fails, display a message to the user. */
                                        Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }
            }
        });
    }
}
