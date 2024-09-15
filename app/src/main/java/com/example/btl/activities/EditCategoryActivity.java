package com.example.btl.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btl.databinding.ActivityEditCategoryBinding;
import com.example.btl.databinding.ActivityPdfEditBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class EditCategoryActivity extends AppCompatActivity {
    private ActivityEditCategoryBinding binding;
    public String categoryId;
    private ProgressDialog progressDialog;
    private static final String TAG = "CATEGORY_EDIT_TAG";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categoryId = getIntent().getStringExtra("categoryId");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        loadCategories();

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });
    }

    private String category ="";
    private void validateData() {
        //get data
        category = binding.categoryEt.getText().toString().trim();

        if(TextUtils.isEmpty(category)){
            Toast.makeText(this, "Enter category", Toast.LENGTH_SHORT).show();
        }
        else{
            updateCategory();
        }

    }

    private void loadCategories() {
        Log.d(TAG, "loadBookInfo: Loading book info");
        DatabaseReference refBooks = FirebaseDatabase.getInstance().getReference("Categories");
        refBooks.child(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = ""+snapshot.child("category").getValue();

                        binding.categoryEt.setText(category);

                        Log.d(TAG, "onDataChange: Loading category");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updateCategory() {
        Log.d(TAG, "updatePdf: Starting update category into db");

        progressDialog.setMessage("Updating");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("category",""+category);

        //bat dau update
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Book updated");
                        progressDialog.dismiss();
                        Toast.makeText(EditCategoryActivity.this, "Category info updated", Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update due to "+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(EditCategoryActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}