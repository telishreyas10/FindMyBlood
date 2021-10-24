package com.blizzard.app.findmyblood;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser current_user;
    private DatabaseReference dbRef;
    private TextView fullname,bloodgroup,address,location;
    private Button changeimage;
    private CircleImageView profile_image;
    private static final int c=1;
    private Uri imgUri;

    private StorageReference imgStore;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fullname=(TextView)findViewById(R.id.profile_name);
        bloodgroup=(TextView)findViewById(R.id.profile_bloodgroup);
        address=(TextView)findViewById(R.id.profile_address);
        location=(TextView)findViewById(R.id.profile_location);
        changeimage=(Button)findViewById(R.id.profile_button);
        profile_image=(CircleImageView)findViewById(R.id.profile_image);

        imgStore= FirebaseStorage.getInstance().getReference("images");


        current_user= FirebaseAuth.getInstance().getCurrentUser();
        String current_uid=current_user.getUid();
        dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String mname=dataSnapshot.child("FullName").getValue().toString();
                String maddress=dataSnapshot.child("Address").getValue().toString();
                String mbloodgroup=dataSnapshot.child("Bloodgroup").getValue().toString();
                String mlocation=dataSnapshot.child("Location").getValue().toString();
                String image=dataSnapshot.child("image").getValue().toString();

                fullname.setText(mname);
                address.setText(maddress);
                bloodgroup.setText(mbloodgroup);
                location.setText(mlocation);
                changeimage.setText("Change Image");

                Picasso.get().load(image).into(profile_image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        changeimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Image"),c);

                //

                /*CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);*/

            }
        });


    }


 @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==c && resultCode==RESULT_OK){
            imgUri=data.getData();

            CropImage.activity(imgUri)
                    .setAspectRatio(1,1)
                    .start(this);

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                String current_id=current_user.getUid();

                final StorageReference fileRef=imgStore.child(current_id+ ".jpg");


                uploadTask=fileRef.putFile(resultUri);
                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return fileRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri=task.getResult();
                            String mUri=downloadUri.toString();

                            dbRef=FirebaseDatabase.getInstance().getReference("Users").child(current_user.getUid());
                            HashMap<String,Object> map=new HashMap<>();
                            map.put("image",mUri);
                            dbRef.updateChildren(map);
                        }
                        else {
                            Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver= getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    /*private void uploadImage(){
        if(imgUri!= null){
            final StorageReference fileRef=imgStore.child(System.currentTimeMillis()+"."+getFileExtension(imgUri));
            uploadTask=fileRef.putFile(imgUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String mUri=downloadUri.toString();

                        dbRef=FirebaseDatabase.getInstance().getReference("Users").child(current_user.getUid());
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("image",mUri);
                        dbRef.updateChildren(map);
                    }
                    else {
                        Toast.makeText(getBaseContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }*/
}
