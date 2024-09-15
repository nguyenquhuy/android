package com.example.btl.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.btl.R;
import com.example.btl.databinding.ActivityCategoryAddBinding;
import com.example.btl.databinding.ActivityTestAddBinding;
import com.example.btl.databinding.ActivityBtlBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class TestAdd extends AppCompatActivity {
    private ActivityTestAddBinding binding;

    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance();

        //configure progress dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        binding.listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TestAdd.this, DashboardTest.class));
            }
        });
        //click go back
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //click, upload category
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });

    }

    private String value = "",description="", soluong="", dongia="";
    private void validateData() {
        //Truoc khi them validate data
        //get data
        value = binding.titleEt.getText().toString().trim();
        description = binding.descriptionEt.getText().toString().trim();
        soluong = binding.soluongEt.getText().toString().trim();
        dongia = binding.dongiaEt.getText().toString().trim();

        if(TextUtils.isEmpty(value)){
            int color = Color.parseColor("#99cc00");
            Toast.makeText(this, "Please enter value...", Toast.LENGTH_SHORT).show();
            binding.submitBtn.setBackgroundColor(R.style.Buttonwhite);
        }
        else{
            addTestFirebase();
            startActivity(new Intent(this, DashboardAdminActivity.class));
        }
    }

    private void addTestFirebase() {
        {
            //show progess
            progressDialog.setMessage("Adding test...");
            progressDialog.show();

            //get timestamp
            long timestamp = System.currentTimeMillis();

            //setup info to add in firebase db
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id",""+timestamp);
            hashMap.put("name",""+value);
            hashMap.put("description",""+description);
            hashMap.put("soluong",""+soluong);
            hashMap.put("dongia",""+dongia);

            //add to firebase db.... Database Root > Categories > categoryID > categoryinfo
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tests");
            ref.orderByChild("test").equalTo(value)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Toast.makeText(TestAdd.this, "Test Existed", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                ref.child(""+timestamp)
                                        .setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //them category thanh cong
                                                progressDialog.dismiss();
                                                Toast.makeText(TestAdd.this, "Add successfully", Toast.LENGTH_SHORT).show();

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //them category that bai
                                                progressDialog.dismiss();
                                                Toast.makeText(TestAdd.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }

    }
}