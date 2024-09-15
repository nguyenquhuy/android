package com.example.btl.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.btl.adapters.AdapterCategory;
import com.example.btl.adapters.AdapterTest;
import com.example.btl.databinding.ActivityDashboardTestBinding;
import com.example.btl.models.ModelTest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DashboardTest extends AppCompatActivity {
    //view binding
    private ActivityDashboardTestBinding binding;

    private FirebaseAuth firebaseAuth;

    //arraylist
    private ArrayList<ModelTest> categoryArrayList;
    //adapter
    private AdapterTest adapterCategory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //init firebaseauth
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        loadCategories();

        //edit text change listen, search
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //called as  and when user type each letter
                try {
                }
                catch (Exception e)
                {

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //logout
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                checkUser();
            }
        });

        //hien add category
        binding.addTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardTest.this, TestAdd.class));
            }
        });
        //hien add category
        binding.addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardTest.this, CategoryAddActivity.class));
            }
        });

        //click add pdf
        binding.addPdfFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardTest.this, PdfAddActivity.class));
            }
        });

        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardTest.this, ProfileActivity.class));
            }
        });
    }

    //list the loai
    private void loadCategories() {
        categoryArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Tests");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ModelTest model = ds.getValue(ModelTest.class);
                    categoryArrayList.add(model);
                }
                adapterCategory = new AdapterTest(DashboardTest.this, categoryArrayList);
                binding.categoriesRv.setAdapter(adapterCategory);
                adapterCategory.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void checkUser() {
        //get current user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            //khong dang nhap, vao man hinh chinh
            startActivity(new Intent(this, MainActivity2.class));
            finish();
        }
        else {
            //dang nhap, lay thong tin user
            String email = firebaseUser.getEmail();
            //set in textview of toolbar
            binding.subtittleTv.setText(email);
        }
    }
}