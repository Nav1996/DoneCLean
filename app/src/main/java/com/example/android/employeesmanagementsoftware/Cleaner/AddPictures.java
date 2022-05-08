package com.example.android.employeesmanagementsoftware.Cleaner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.android.employeesmanagementsoftware.R;
import android.widget.*;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class AddPictures extends AppCompatActivity {
    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int RESULT_OK = -1;
    String task_id;
    ImageView selectedImage;
    Button submitbutn;

    String currentPhotoPath;
    Boolean isStarted;
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    private final int REQUEST_IMAGE_CAPTURE = 222;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pictures);

        Intent intent = getIntent();
        task_id = intent.getStringExtra("Task_ID");

        isStarted = false;

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        selectedImage = findViewById(R.id.imagecapture);
        submitbutn = findViewById(R.id.beforepiccap);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        checkIfStarted();

        selectedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagecapture();
//                dispatchTakePictureIntent();
            }
        });
        
        submitbutn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImagetoFirebase();
            }
        });

//        mstorage = FirebaseStorage.getInstance().getReference();
    }


    private void checkIfStarted() {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("uploads").child(task_id);

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isStarted = true;
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });

    }
//
//    private void uploadImage() {
//        if (filePath != null) {
//
//            // Code for showing progressDialog while uploading
//            ProgressDialog progressDialog
//                    = new ProgressDialog(this);
//            progressDialog.setTitle("Uploading...");
//            progressDialog.show();
//
//            // Defining the child of storageReference
//            String randID = UUID.randomUUID().toString();
//            StorageReference ref = storageReference.child(task_id+ "/" + randID);
//
//            // adding listeners on upload
//            // or failure of image
//            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    // Image uploaded successfully
//                    // Dismiss dialog
//                    progressDialog.dismiss();
//                    Toast.makeText(AddPictures.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
//
//                    uploadToDB(randID);
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e)
//                {
//
//                    // Error, Image not uploaded
//                    progressDialog.dismiss();
//                    Toast.makeText(AddPictures.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                // Progress Listener for loading
//                // percentage on the dialog box
//                @Override
//                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress
//                            = (100.0
//                            * taskSnapshot.getBytesTransferred()
//                            / taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage(
//                            "Uploaded "
//                                    + (int)progress + "%");
//                }
//            });
//        }
//    }
//
    private void uploadToDB(String randID) {
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("uploads").child(task_id);
        Upload upload = new Upload();

        dbref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                if(isStarted) {
                    upload.setId(dataSnapshot.child("id").getValue().toString());
                    upload.setStart(dataSnapshot.child("start").getValue().toString());
                    upload.setEnd(randID);

                    dbref.setValue(upload);
                }
                else {
                    upload.setId(task_id);
                    upload.setStart(randID);
                    upload.setEnd(null);

                    dbref.setValue(upload);
                }

                Intent intent = new Intent(AddPictures.this, CleanerMainPage.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }
//
//    private void selectedImage() {
////        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////        Toast.makeText(getApplicationContext(), "photo ", Toast.LENGTH_SHORT).show();
////        // Ensure that there's a camera activity to handle the intent
////        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
////            // Create the File where the photo should go
////            File photoFile = null;
////            try {
////                photoFile = createImageFile();
////            } catch (IOException ex) {
////                // Error occurred while creating the File
////
////            }
////            // Continue only if the File was successfully created
////            if (photoFile != null) {
////                Uri photoURI = FileProvider.getUriForFile(this,
////                        "com.example.android.fileprovider",
////                        photoFile);
////                Toast.makeText(getApplicationContext(), "photo "+photoURI, Toast.LENGTH_SHORT).show();
////                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
////                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
////            }
////        }
//        // Defining Implicit Intent to mobile gallery
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(
//                Intent.createChooser(
//                        intent,
//                        "Select Image from here..."),
//                PICK_IMAGE_REQUEST);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Toast.makeText(getApplicationContext(), "GetData "+data.getData(), Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Request "+requestCode, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Result "+resultCode, Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(), "Data "+data, Toast.LENGTH_SHORT).show();
//        if (requestCode == PICK_IMAGE_REQUEST
//                && resultCode == RESULT_OK
//                && data != null
//                && data.getData() != null) {
//
//            Toast.makeText(getApplicationContext(), "Onactivity called", Toast.LENGTH_SHORT).show();
//            // Get the Uri of data
//            filePath = data.getData();
//            try {
//
//                // Setting image on image view using Bitmap
//                Bitmap bitmap = MediaStore
//                        .Images
//                        .Media
//                        .getBitmap(
//                                getContentResolver(),
//                                filePath);
//                selectedImage.setImageBitmap(bitmap);
//            }
//
//            catch (IOException e) {
//                // Log the exception
//                e.printStackTrace();
//            }
//        }
//    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void imagecapture() {
        Toast.makeText(this, "Redirecting to camera", Toast.LENGTH_SHORT).show();
        askpermission();
    }
////
////    public void submitbtn(View view) {
////
////    }
////
    public void askpermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        }
        else {
            openCamera();
        }
    }
//
    private void onActivityResulting() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
//
//
//    //
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            }
            else {
                Toast.makeText(this, "Camera permission is r" +
                        "equired", Toast.LENGTH_SHORT).show();
            }
        }
    }
//
    public void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){

                Bitmap image = (Bitmap) data.getExtras().get("data");
                selectedImage.setImageBitmap(image);
//                File f = new File(currentPhotoPath);
//                selectedImage.setImageURI(Uri.fromFile(f));
//                Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));
//
//                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                Uri contentUri = Uri.fromFile(f);
//                mediaScanIntent.setData(contentUri);
//                this.sendBroadcast(mediaScanIntent);
//
//                uploadImageToFirebase(f.getName(),contentUri);



            }

        }
    }

//    private void dispatchTakePictureIntent() {
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // Ensure that there's a camera activity to handle the intent
//        Toast.makeText(this, "Waiting", Toast.LENGTH_SHORT).show();
//        // Create the File where the photo should go
//        File photoFile = null;
//        try {
//            photoFile = createImageFile();
//        } catch (IOException ex) {
////
//        }
//            // Continue only if the File was successfully created
//        if (photoFile != null) {
//
//            Uri photoURI = FileProvider.getUriForFile(this,
//                    "com.example.fileprovider",
//                    photoFile);
//            Toast.makeText(this, "Waiting33", Toast.LENGTH_SHORT).show();
////            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
////            startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
//        }
//        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
//
//        }
//    }

//    private void uploadImageToFirebase(String name, Uri contentUri) {
//        final StorageReference image = storageReference.child(task_id).child(name);
//        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d("tag", "onSuccess: Uploaded Image URl is " + uri.toString());
//                    }
//                });
//
//                Toast.makeText(AddPictures.this, "Image Is Uploaded.", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(AddPictures.this, "Upload Failled.", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }

//    private File createImageFile() throws IOException {
//
//    }
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
////        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//        Toast.makeText(this, "Waiting22", Toast.LENGTH_SHORT).show();
//
//        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

    private void uploadImagetoFirebase() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

//         Code for showing progressDialog while uploading
        ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        StorageReference stref = storageReference.child(task_id+ "/" + imageFileName);

        // Get the data from an ImageView as bytes
        selectedImage.setDrawingCacheEnabled(true);
        selectedImage.buildDrawingCache();
        Bitmap bitmap = selectedImage.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = stref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(), "Upload unsuccessful", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "Successfully uploaded", Toast.LENGTH_SHORT).show();
                uploadToDB(imageFileName);
                progressDialog.dismiss();
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot snapshot) {
                double progress
                            = (100.0
                            * snapshot.getBytesTransferred()
                            / snapshot.getTotalByteCount());
                    progressDialog.setMessage(
                            "Uploaded "
                                    + (int)progress + "%");
            }
        });
    }
}