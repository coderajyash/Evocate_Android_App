package com.app.evocate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

import model.Evocate;
import util.EvocateApi;

public class homePage extends AppCompatActivity implements View.OnClickListener {
    private static final int GALLERY_CODE = 1;
    private Button save_button;
    private TextView user_view;
    private EditText title,description;
    private ImageView camera_button,imageView;
    private ProgressBar progressBar;
    private String currentusername,currentuserID;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Evocate");
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        storageReference= FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        save_button=findViewById(R.id.save_button);
        title=findViewById(R.id.title_text);
        imageView=findViewById(R.id.imageView);
        description=findViewById(R.id.description_text);
        user_view=findViewById(R.id.user_view);
        camera_button=findViewById(R.id.postCamera_button);
        save_button.setOnClickListener(this);
        camera_button.setOnClickListener(this);

        if(EvocateApi.getInstance()!=null){
            currentuserID=EvocateApi.getInstance().getUserID();
            currentusername=EvocateApi.getInstance().getUsername();
            user_view.setText(currentusername);
        }
        authStateListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user=firebaseAuth.getCurrentUser();
                if(user!=null){

                }
            }
        };


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_button:

                savePost();
                break;
            case R.id.postCamera_button:
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
                break;

        }
    }

    private void savePost() {
        progressBar.setVisibility(View.VISIBLE);
        final String title_save=title.getText().toString().trim();
        final String description_save=description.getText().toString();
        if(!TextUtils.isEmpty(title_save)&&!TextUtils.isEmpty(description_save)&&imageUri!=null){
            final StorageReference file_path=storageReference.child("Evocate_images")
                    .child("my_image"+ Timestamp.now().getSeconds());
            file_path.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //Evocate Object will be made-model
                          file_path.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                              @Override
                              public void onSuccess(Uri uri) {
                                  String imageURL=uri.toString();
                                  Evocate evocate=new Evocate();
                                  evocate.setTitle(title_save);
                                  evocate.setDescription(description_save);
                                  evocate.setImageURL(imageURL);
                                  evocate.setTimeAdded(new Timestamp(new Date()));
                                  evocate.setUsername(currentusername);
                                  evocate.setUserID(currentuserID);
                                  //Now Invoke Collection Refference
                                  collectionReference.add(evocate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                      @Override
                                      public void onSuccess(DocumentReference documentReference) {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(homePage.this,EvocateList.class));
                                        finish();
                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Log.d("Error", "onFailure: "+e.getMessage());
                                      }
                                  });
                              }
                          });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(homePage.this,"Some Problem Occurred :(",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
        else{
            Toast.makeText(homePage.this,"Some Problem Occurred :(",Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE&&resultCode==RESULT_OK){
            imageUri =data.getData();//Path to image
            imageView.setImageURI(imageUri);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user=firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}

