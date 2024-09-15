package com.example.btl.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.btl.databinding.ActivityImageAddBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.UUID;

public class ImageAddActivity extends AppCompatActivity {

    private ActivityImageAddBinding binding;
    // Uri indicates, where the image will be picked from
    private Uri imgUri = null;
    private ProgressDialog progressDialog;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private static final String TAG = "ADD_IMAGE_TAG";
    String bookId ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        binding = ActivityImageAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        bookId = intent.getStringExtra("bookId");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);



        binding.btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImage();
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    // Select Image method
    private void SelectImage()
    {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("application/image");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    private void uploadImage() {
        //upload pdf to firebase storage
        Log.d(TAG, "uploadPdfToStorage: uploading to storage");
        progressDialog.setMessage("Uploading image");
        progressDialog.show();

        //timestamp
        long timestamp = System.currentTimeMillis();

        //path of pdf in firebase storage
        String filePathAndName = "Images/" + timestamp;
        //storage reference
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
        storageReference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d(TAG, "onSuccess: Image uploaded to storage");
                        Log.d(TAG, "onSuccess: getting image url");

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uploadedImgUrl = ""+uriTask.getResult();

                        //upload to firebase
                        uploadImageInfoToDb(uploadedImgUrl);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: image upload failed due to" + e.getMessage());
                        Toast.makeText(ImageAddActivity.this, "image upload failed due to" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void uploadImageInfoToDb(String uploadedPdfUrl) {
        Log.d(TAG, "updateProfile: Updating image url");
        progressDialog.setMessage("Updating image url");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("imgUrl",""+uploadedPdfUrl);
        if(uploadedPdfUrl != null){
            hashMap.put("imgUrl",""+uploadedPdfUrl);
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Books");
        databaseReference.child(bookId)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "onSuccess: Profile updated");
                        progressDialog.dismiss();
                        Toast.makeText(ImageAddActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Failed to update db due to "+e.getMessage());
                        progressDialog.dismiss();
                        Toast.makeText(ImageAddActivity.this, "Failed to update db due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


}