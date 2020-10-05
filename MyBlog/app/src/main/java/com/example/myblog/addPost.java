package com.example.myblog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class addPost extends AppCompatActivity {

    private ImageButton mPostImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitButton;

    private DatabaseReference mPostDatabase;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private StorageReference mStorage;

    private ProgressDialog progressDialog;
    private Uri mimageURI;
    private static final int GALLERY_CODE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        mPostDatabase= FirebaseDatabase.getInstance().getReference().child("MBlog");
        mStorage= FirebaseStorage.getInstance().getReference();

        mPostImage=findViewById(R.id.postImageButton);
        mPostTitle=findViewById(R.id.postTitle);
        mPostDesc=findViewById(R.id.postDescriptions);
        mSubmitButton=findViewById(R.id.submit_button);

        mPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent=new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we are going to submit the post to post list activity
                startPosting();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK){

            mimageURI=data.getData();
            mPostImage.setImageURI(mimageURI);
        }
    }

    private void startPosting() {

        progressDialog=new ProgressDialog(addPost.this);
        progressDialog.setMessage("Creating your blog");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        final String titleVal=mPostTitle.getText().toString().trim();
        final String descVal=mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(titleVal) && !TextUtils.isEmpty(descVal) && mimageURI!=null){
            //we can start uploading here.....
            /*Blog blog=new Blog("title","description","imageURL","timeStamp","userId");
            mPostDatabase.setValue(blog).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(addPost.this, "Your blog has been added", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });*/
           // final StorageReference filepath=mStorage.child("MBLOG_images").child(mimageURI.getLastPathSegment());
            /*filepath.putFile(mimageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl();
                    Log.d("imgurl:",downloadUrl.toString());
                    DatabaseReference newPost=mPostDatabase.push();
                    Map<String , String> dataToSave=new HashMap<>();
                    dataToSave.put("title",titleVal);
                    dataToSave.put("desc",descVal);
                    dataToSave.put("image",downloadUrl.toString());
                    dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                    dataToSave.put("userid",mUser.getUid());

                    newPost.setValue(dataToSave);
                    progressDialog.dismiss();
                    startActivity( new Intent(addPost.this,PostListActivity.class));
                }
            });*/
            final StorageReference filepath=mStorage.child("MBLOG_images").child(mimageURI.getLastPathSegment());
            filepath.putFile(mimageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUrl=uri;
                            DatabaseReference newPost=mPostDatabase.push();
                            Map<String , String> dataToSave=new HashMap<>();
                            dataToSave.put("title",titleVal);
                            dataToSave.put("desc",descVal);
                            dataToSave.put("image",downloadUrl.toString());
                            dataToSave.put("timestamp",String.valueOf(java.lang.System.currentTimeMillis()));
                            dataToSave.put("userid",mUser.getUid());

                            newPost.setValue(dataToSave);
                            progressDialog.dismiss();
                            startActivity( new Intent(addPost.this,PostListActivity.class));

                        }
                    });
                }
            });



        }else{
            Toast.makeText(this, "Incomplete information to create blog", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }
}
