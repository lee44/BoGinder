package com.apps.jlee.boginder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apps.jlee.boginder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity
{
    private EditText name, email, password;
    private Button register;
    private RadioGroup radioGroup;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = findViewById(R.id.register);
        radioGroup = findViewById(R.id.gender_radio_group);

        firebaseAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(email.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0)
                {
                    int selectId = radioGroup.getCheckedRadioButtonId();
                    final RadioButton radioButton = findViewById(selectId);

                    if(radioButton.getText() == null)
                    {
                        return;
                    }

                    firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(RegistrationActivity.this, "Sign up Error", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String userId = firebaseAuth.getCurrentUser().getUid();
                                DatabaseReference currentUserDB = FirebaseDatabase.getInstance().getReference().child("Users");
                                currentUserDB.push().setValue(userId);currentUserDB.child(userId+"/Gender").setValue(radioButton.getText().toString());
                                currentUserDB.child(userId+"/Name").setValue(name.getText().toString());
                                currentUserDB.child(userId+"/ProfileImageUrl").setValue("Default");

                            }
                        }
                    });
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(user != null)
                {
                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };
    }

    @Override
    public void onStart()
    {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(RegistrationActivity.this, LoginRegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
