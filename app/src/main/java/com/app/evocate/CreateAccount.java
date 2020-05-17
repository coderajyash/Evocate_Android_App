package com.app.evocate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import util.EvocateApi;

public class CreateAccount extends AppCompatActivity {
    private Button login,createAccount;
    private EditText username,email,password;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        login=findViewById(R.id.login_button);
        username=findViewById(R.id.username);
        progressBar=findViewById(R.id.create_bar);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        createAccount=findViewById(R.id.createAccount_botton);
        firebaseAuth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateAccount.this,LoginActivity.class));
            }
        });
        authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser=firebaseAuth.getCurrentUser();
                if(currentUser!=null){
                    //user already loggedin
                }
                else{

                }
            }
        };
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(email.getText().toString())&&!TextUtils.isEmpty(password.getText().toString())&&
                !TextUtils.isEmpty(username.getText().toString())){
                    String em=email.getText().toString();
                    String pass=password.getText().toString();
                    String user=username.getText().toString();
                createUserEmailAccount(em,pass,user);
                }
                else{
                    Toast.makeText(CreateAccount.this,"All fields are mandatory.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }
    public void createUserEmailAccount(String email, String password, final String username){
    if(!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(username)){
    progressBar.setVisibility(View.VISIBLE);
    firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){//Go to home page
                        currentUser=firebaseAuth.getCurrentUser();
                        assert currentUser != null;
                        final String currentUserID=currentUser.getUid();
                        //Create A User MAP object for Collection
                        Map<String,String> userObject=new HashMap<>();
                        userObject.put("userID",currentUserID);
                        userObject.put("username",username);
                        collectionReference.add(userObject).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(Objects.requireNonNull(task.getResult()).exists()){
                                            progressBar.setVisibility(View.INVISIBLE);
                                            String name=task.getResult().getString("username");

                                            EvocateApi evocateApi=EvocateApi.getInstance();
                                            evocateApi.setUserID(currentUserID);
                                            evocateApi.setUsername(username);

                                            Intent intent=new Intent(CreateAccount.this,homePage.class);
                                            startActivity(intent);
                                        }
                                        else{
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }
                    else{

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {

        }
    });
    }
    else{

    }
    }
}
