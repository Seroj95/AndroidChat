package com.example.awesomechat;
//164 sksel nayel
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
private ListView messageListView;
private AwesomeMessageAdapter adapter;
private ProgressBar progressBar;
private static final int RC_IMAGE_PICKER=123;
private ImageButton sendImageButton;
private Button sendMessageButon;
private EditText messageEditText;
private String userName;
FirebaseDatabase database;
DatabaseReference messageDatabaseReferance;
ChildEventListener messagesChildEventListener;

    DatabaseReference userDatabaseReferance;
    ChildEventListener userChildEventListener;
    FirebaseStorage storage;
    StorageReference chatImageStorageReference;


//DatabaseReference userDatabaseReferance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        messageDatabaseReferance=database.getReference().child("message");
       userDatabaseReferance=database.getReference().child("users");

       chatImageStorageReference=storage.getReference().child("chat_images");
//        userDatabaseReferance=database.getReference().child("users");


//        messageDatabaseReferance. child("message1").setValue("Hello Firbes!");
//        messageDatabaseReferance. child("message2").setValue("Hello Word!");
//        userDatabaseReferance.child("user1").setValue("Joe");
        progressBar=findViewById(R.id.progresBar);
        sendImageButton=findViewById(R.id.sendPhotoButton);
        sendMessageButon=findViewById(R.id.sendMessageButton);
        messageEditText=findViewById(R.id.messageEditText);
Intent intent =getIntent();
if (intent !=null){
    userName=intent.getStringExtra("userName");
}else {
    userName="Default  User";
}

messageListView=findViewById(R.id.messageListView);
List<AwsomeMessage>awsomeMessages=new ArrayList<>();
adapter=new AwesomeMessageAdapter(this,R.layout.message_item,awsomeMessages);
messageListView.setAdapter(adapter);
progressBar.setVisibility(ProgressBar.INVISIBLE);
messageEditText.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
if (s.toString().trim().length()>0){
    sendImageButton.setEnabled(true);
}else {
    sendImageButton.setEnabled(false);
}
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});
sendMessageButon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AwsomeMessage message=new AwsomeMessage();
        message.setText(messageEditText.getText().toString());
        message.setName(userName);
        message.setImageUrl(null);


messageDatabaseReferance.push().setValue(message);
        messageEditText.setText("");
    }
});
sendImageButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent  intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY,true);
        startActivityForResult(Intent.createChooser(intent,"Chosse an image"),RC_IMAGE_PICKER);

    }
});
userChildEventListener=new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
User user =dataSnapshot.getValue( User.class);
if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
    userName=user.getName();

}
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
};

userDatabaseReferance.addChildEventListener(userChildEventListener);
messagesChildEventListener=new ChildEventListener() {
    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        AwsomeMessage message=dataSnapshot.getValue(AwsomeMessage.class);
        adapter.add(message);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
};
messageDatabaseReferance.addChildEventListener(messagesChildEventListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
       inflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                return  true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RC_IMAGE_PICKER&&resultCode==RESULT_OK){
            Uri selectedImageUri=data.getData();
            final StorageReference imageReference=chatImageStorageReference
                    .child(selectedImageUri.getLastPathSegment());
            //contnt://image/some_folder/3

            UploadTask uploadTask=imageReference.putFile(selectedImageUri);

            uploadTask = imageReference.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imageReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        AwsomeMessage message = new AwsomeMessage();
                        message.setImageUrl(downloadUri.toString());
                        message.setName(userName);
                        messageDatabaseReferance.push().setValue(message);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}
