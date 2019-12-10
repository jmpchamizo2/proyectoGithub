package com.jmpcdev.wardrobe;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;


import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.BitSet;
import java.util.Date;
import java.util.List;
import java.util.Locale;



public class GarmentActivity extends AppCompatActivity implements MultiSpinner.MultiSpinnerListener {

    private final int PERMISSION_READ_EXTERNAL_STORAGE = 1;
    private final int PERMISSION_WRITE_EXTERNAL_STORAGE = 2;
    private final int PERMISSION_CAMERA = 3;
    private final int CODE_EXTERNAL_STORAGE = 10;
    private final int CODE_CAMERA = 20;
    private final int PHOTO_SIZE = 520;



    private ImageView imvGarmentHome;
    private EditText edtNameGarment, edtDesc, edtColor, edtTissue, edtBrandname;
    private Button btnGarmentContinue, btnCombine;
    private MultiSpinner multiSpinnerTemperature;
    private Spinner spinnerType;
    private ImageButton imbCamera, imbSelectPhoto;
    private ImageView imvGarmentPreview;

    private List<Integer> itemsSelected = new ArrayList<>();
    private int itemSelected = -1;



    private List<TemperaturesGarment> temperaturesGarment = new ArrayList<TemperaturesGarment>();


    private ArrayList<String> idsSelected;



    private FirebaseAuth mAuth;

    private String currentPhotoPath;

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garment);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        getViews();
        checkIntentExtras();
        prepareSpinners();
        prepareOnClickListener();
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }




    private void uploadGarmentFirebase(){
        DatabaseReference mDataBase = FirebaseDatabase.getInstance().getReference();
        Garment garment = createGarment();

        final DatabaseReference mDataBaseGarmentsGarmentId = mDataBase.child("garments").child(garment.getId());
        mDataBaseGarmentsGarmentId.child("id").setValue(garment.getId());
        mDataBaseGarmentsGarmentId.child("name").setValue(garment.getName());
        mDataBaseGarmentsGarmentId.child("type").setValue(garment.getType());
        mDataBaseGarmentsGarmentId.child("description").setValue(garment.getDescription());
        mDataBaseGarmentsGarmentId.child("color").setValue(garment.getColor());
        mDataBaseGarmentsGarmentId.child("tissue").setValue(garment.getTissue());
        mDataBaseGarmentsGarmentId.child("temperature").setValue(garment.getTemperature());
        mDataBaseGarmentsGarmentId.child("brandname").setValue(garment.getBrandName());
        mDataBaseGarmentsGarmentId.child("users").setValue(garment.getUsers());
        mDataBaseGarmentsGarmentId.child("combine").setValue(garment.getCombine());
        updateGarmentsOfUser(garment);
        updateCombineOfGarments(garment.getId());
        updateImageUser(garment, mDataBaseGarmentsGarmentId);


    }


    private Garment createGarment(){
        String name = edtNameGarment.getText().toString();
        String type = spinnerType.getSelectedItem().toString();

        String description = edtDesc.getText().toString();
        String color = edtColor.getText().toString();
        String tissue = edtTissue.getText().toString();


        String brandname = edtBrandname.getText().toString();
        final Garment garment = new Garment("temp", name, type, description, color, tissue, temperaturesGarment, brandname);


        garment.addUser(mAuth.getCurrentUser().getUid());
        if(idsSelected != null){
            for(String id : idsSelected){
                garment.addCombine(id);
            }
        }

        return garment;
    }


    private void updateGarmentsOfUser(final Garment garment){
        FirebaseUser currentUser = mAuth.getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid()).child("garments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<String> garmentsId = (List<String>) dataSnapshot.getValue();
                        if(garmentsId == null){
                            garmentsId = new ArrayList<>();
                        }
                        garmentsId.add(garment.getId());
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        FirebaseDatabase.getInstance().getReference().child("users")
                                .child(currentUser.getUid()).child("garments").setValue(garmentsId);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    private void  updateCombineOfGarments(final String garmentId){
        if(idsSelected == null){
            return;
        }
        for(final String id : idsSelected){
            FirebaseDatabase.getInstance().getReference().child("garments").child(id).child("combine")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        List<String> combine;
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            combine = (dataSnapshot.getValue()==null)? new ArrayList<String>(): (List<String>) dataSnapshot.getValue();
                            combine.add(garmentId);
                            FirebaseDatabase.getInstance().getReference().child("garments")
                                    .child(id).child("combine").setValue(combine);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
    }



    @Override
    public void onItemsSelected(boolean[] selected) {

        for(int i = 0; i < selected.length; i++){
            if(selected[i]){
                temperaturesGarment.add(TemperaturesGarment.values()[i]);
                itemsSelected.add(i);
            }
        }
    }


    private void prepareOnClickListener(){
        imvGarmentHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GarmentActivity.this, MainActivity.class));
            }
        });

        btnGarmentContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadGarmentFirebase();
                startActivity(new Intent(GarmentActivity.this, MainActivity.class));
            }
        });

        btnCombine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(GarmentActivity.this, CombineActivity.class);
                prepareExtras(i);
                startActivity(i);
            }
        });

        imbSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionReadStorage();
            }
        });
        
        imbCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionCamera();
            }
        });


    }

    private void prepareExtras(Intent i){
        i.putStringArrayListExtra("ids", idsSelected);
        if(edtNameGarment.getText() != null){
            i.putExtra("nameGarment", edtNameGarment.getText().toString());
        }
        if(edtBrandname.getText() != null){
            i.putExtra("brandName", edtBrandname.getText().toString());
        }
        if(edtDesc.getText() != null){
            i.putExtra("description", edtDesc.getText().toString());
        }
        if(edtColor.getText() != null){
            i.putExtra("color", edtColor.getText().toString());
        }
        if(edtTissue.getText() != null){
            i.putExtra("tissue", edtTissue.getText().toString());
        }
        if(itemsSelected.size() > 0){
            i.putIntegerArrayListExtra("itemsSelected", (ArrayList<Integer>) itemsSelected);
        }
        if(bitmap != null){
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            i.putExtra("bitmap", byteArray);
        }

        itemSelected = Arrays.asList(getResources().getStringArray(R.array.types)).indexOf(spinnerType.getSelectedItem());
        i.putExtra("itemSelected", itemSelected);
    }



    private void checkIntentExtras(){
        Bundle data = this.getIntent().getExtras();
        if (data != null){
            if(data.getStringArrayList("ids") != null && data.getStringArrayList("ids").size()>0){
                idsSelected  = data.getStringArrayList("ids");
                String s = "";
                for(String id : idsSelected){
                    s += id + ", ";
                }
                btnCombine.setText(s.substring(0,s.length()-2));
            }
            if(data.getString("nameGarment") != null){
                edtNameGarment.setText(data.getString("nameGarment"));
            }
            if(data.getString("brandName") != null){
                edtBrandname.setText(data.getString("brandName"));
            }
            if(data.getString("description") != null){
                edtDesc.setText(data.getString("description"));
            }
            if(data.getString("color") != null){
                edtColor.setText(data.getString("color"));
            }
            if(data.getString("tissue") != null){
                edtTissue.setText(data.getString("tissue"));
            }
            itemSelected = data.getInt("itemSelected");
            if(data.getIntegerArrayList("itemsSelected") != null && data.getIntegerArrayList("itemsSelected").size() > 0) {
                itemsSelected = data.getIntegerArrayList("itemsSelected");
            }
            if(getIntent().getByteArrayExtra("bitmap") != null){
                byte[] byteArray = getIntent().getByteArrayExtra("bitmap");
                bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                imvGarmentPreview.setImageBitmap(bitmap);
            }



        }
    }


    private void prepareSpinners(){
        String title = "";
        List<String> items = Arrays.asList(getResources().getStringArray(R.array.temperatures));

        if(itemsSelected.size() > 0){
            for(int item : itemsSelected){
                title += items.get(item) + ", ";
            }
            title = title.substring(0, title.length()-2);
        } else {
            title = getString(R.string.select_temperature);
        }
        multiSpinnerTemperature.setItems(Arrays.asList(getResources().getStringArray(R.array.temperatures)), title,  this, itemsSelected);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.types, R.layout.custom_spinner_items);
        spinnerType.setAdapter(adapter);
        if (itemSelected != -1) {
            spinnerType.setSelection(itemSelected);
        }

    }



    private void getViews(){
        imvGarmentHome = findViewById(R.id.imvGarmentHome);
        edtColor = findViewById(R.id.edtColor);
        edtDesc = findViewById(R.id.edtDesc);
        edtNameGarment = findViewById(R.id.edtNameGarment);
        edtBrandname = findViewById(R.id.edtStore);
        edtTissue = findViewById(R.id.edtTissue);
        btnCombine = findViewById(R.id.btnCombine);
        btnGarmentContinue = findViewById(R.id.btnGarment);
        multiSpinnerTemperature = (MultiSpinner) findViewById(R.id.multispinnerTemperature);
        imvGarmentPreview = findViewById(R.id.imvGarmentPreView);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        imbSelectPhoto = (ImageButton) findViewById(R.id.imbSelectPhoto);
        imbCamera = (ImageButton) findViewById(R.id.imbCamera);
    }


    private void checkPermissionReadStorage(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GarmentActivity.this);
                builder.setMessage(R.string.message_read_storage)
                        .setTitle(R.string.store_dialog_title);
                builder.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ActivityCompat.requestPermissions(GarmentActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSION_READ_EXTERNAL_STORAGE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_READ_EXTERNAL_STORAGE);

            }
        } else {
            loadImage();
        }

    }

    private void checkPermissionWriteStorage(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GarmentActivity.this);
                builder.setMessage(R.string.message_write_storage)
                        .setTitle(R.string.store_dialog_title);
                builder.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ActivityCompat.requestPermissions(GarmentActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                PERMISSION_WRITE_EXTERNAL_STORAGE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();


            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSION_WRITE_EXTERNAL_STORAGE);

            }
        } else {
            dispatchTakePictureIntent();
        }

    }



    private void checkPermissionCamera(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                AlertDialog.Builder builder = new AlertDialog.Builder(GarmentActivity.this);
                builder.setMessage(R.string.message_camera)
                        .setTitle(R.string.camera_dialog_title);
                builder.setPositiveButton(R.string.cont, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ActivityCompat.requestPermissions(GarmentActivity.this,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    }
                });


                AlertDialog dialog = builder.create();
                dialog.show();

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        PERMISSION_CAMERA);

            }
        } else {
            checkPermissionWriteStorage();
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        loadImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSION_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchTakePictureIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case PERMISSION_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkPermissionWriteStorage();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

        }
    }


    private void loadImage() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/");
        startActivityForResult(i, CODE_EXTERNAL_STORAGE);

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.jmpcdev.wardrobe.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CODE_CAMERA);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case (CODE_EXTERNAL_STORAGE):
                    Uri path = data.getData();
                    imvGarmentPreview.setImageURI(path);
                    reducePhoto(path);
                    return;
                case (CODE_CAMERA):

                    galleryAddPic();
                    setPic();
                    return;
            }

        } else {

        }


    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri path = Uri.fromFile(f);
        mediaScanIntent.setData(path);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        reducePhoto(currentPhotoPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        imvGarmentPreview.setImageBitmap(bitmap);
    }


    private void reducePhoto( Uri uri) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();

        try {
            BitmapFactory.decodeStream(GarmentActivity.this.getContentResolver().openInputStream(uri), null, bmOptions);
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            int scaleFactor = Math.min(photoW/PHOTO_SIZE, photoH/PHOTO_SIZE);


            bmOptions.inSampleSize = scaleFactor;
            bitmap = BitmapFactory.decodeStream(GarmentActivity.this.getContentResolver().openInputStream(uri), null, bmOptions);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private void reducePhoto(String route) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap temp = BitmapFactory.decodeFile(route, bmOptions);
        int photoW = temp.getWidth();
        int photoH = temp.getHeight();

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/PHOTO_SIZE, photoH/PHOTO_SIZE);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        bitmap = BitmapFactory.decodeFile(route, bmOptions);
    }

    private void updateImageUser(Garment garment, final DatabaseReference mDataBaseGarmentsGarmentId) {
        StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
        final StorageReference garmentsRef = mStorageRef.child("garments").child(garment.getId());

        if (bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = garmentsRef.putBytes(data);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return garmentsRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        mDataBaseGarmentsGarmentId.child("image").setValue(downloadUri.toString());
                    } else {
                        //mDataBaseGarmentsGarmentId.child("image").setValue("");
                    }

                }
            });
        } else {

            mDataBaseGarmentsGarmentId.child("image").setValue("");
        }
    }



    private void updateUI(FirebaseUser currentUser) {
        if (currentUser == null) {
            startActivity(new Intent(GarmentActivity.this, LoginActivity.class));
        }

    }

}