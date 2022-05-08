package com.example.android.employeesmanagementsoftware.taskDB;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.employeesmanagementsoftware.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

public class TaskCheckImages extends AppCompatActivity {
    StorageReference mstorage, mstorage2;
    ImageView startImage, endImage;
    String task_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_check_images);

        Intent intent = getIntent();
        task_id = intent.getStringExtra("TaskID");

        startImage = findViewById(R.id.startImage);
        endImage = findViewById(R.id.endImage);

        //Check if an image exists
        mstorage2 = FirebaseStorage.getInstance().getReference().child(task_id).child("JPEG_START");
        Task<Uri> dbref = mstorage2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Retrieve the image
                mstorage = FirebaseStorage.getInstance().getReference().child(task_id).child("JPEG_START");

                try {
                    final File localfile = File.createTempFile("Task", "Start");
                    mstorage.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Image retrieved successfully", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    startImage.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Image retrieved Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(TaskCheckImages.this, "Images not found", Toast.LENGTH_SHORT).show();
            }
        });


        mstorage2 = FirebaseStorage.getInstance().getReference().child(task_id).child("JPEG_END");
        Task<Uri> dbref2 = mstorage2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                //Retrieve the image
                mstorage = FirebaseStorage.getInstance().getReference().child(task_id).child("JPEG_END");

                try {
                    final File localfile = File.createTempFile("Task", "Start");
                    mstorage.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Toast.makeText(getApplicationContext(), "Image retrieved successfully", Toast.LENGTH_SHORT).show();
                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    endImage.setImageBitmap(bitmap);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Image retrieved Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(TaskCheckImages.this, "Images not found", Toast.LENGTH_SHORT).show();
            }
        });



    }
}