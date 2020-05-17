package com.app.evocate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import model.Evocate;
import ui.Adapter;
import util.EvocateApi;

import static com.app.evocate.R.menu.menu;

public class EvocateList extends AppCompatActivity {
    private List<Evocate> evocateList;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseUser user;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private CollectionReference collectionReference=db.collection("Evocate");
    private TextView nothing_added;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evocate_list);
        firebaseAuth=FirebaseAuth.getInstance();
        user=firebaseAuth.getCurrentUser();
        evocateList = new ArrayList<>();
        nothing_added=findViewById(R.id.nothing_present_text);
         recyclerView=findViewById(R.id.recyclerView);
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_add:
                if(user!=null&&firebaseAuth!=null){
                startActivity(new Intent(EvocateList.this,homePage.class));
                }
                break;
            case R.id.action_signout:
                if(user!=null&&firebaseAuth!=null){
                    firebaseAuth.signOut();
                startActivity(new Intent(EvocateList.this,LoginActivity.class));}
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        collectionReference.whereEqualTo("userID", EvocateApi.getInstance().getUserID()).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                        for(QueryDocumentSnapshot obj:queryDocumentSnapshots){
                            Evocate evocate=obj.toObject(Evocate.class);
                            evocateList.add(evocate);
                        }
                        //Recycler View

                            adapter=new Adapter(EvocateList.this,evocateList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        }
                        else{
                            nothing_added.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}
