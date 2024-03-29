package com.example.awesomechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {


    private FirebaseAuth auth;
   private DatabaseReference usersDatabaseReference;
   private ChildEventListener usersChildEventListener;
   private ArrayList<User>userArrayList;
   private RecyclerView userRececlyerView;
   private UserAdapter userAdapter;
   private RecyclerView.LayoutManager userLayoutManager;
    private OnUserClickListener OnUserClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        auth=FirebaseAuth.getInstance();
        userArrayList=new ArrayList<>();
        buildRecyclerView();
        attachUserDatabaseReferenceListener();

    }

    private void attachUserDatabaseReferenceListener() {
        usersDatabaseReference= FirebaseDatabase.getInstance().getReference().child("users");
        if (usersChildEventListener==null){
            usersChildEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    User user =dataSnapshot.getValue(User.class);
                    if (!user.getId().equals(auth.getCurrentUser().getUid())){
                        user.setAvatarMockUpRecourse(R.drawable.ic_person_black_24dp);
                        userArrayList.add(user);
                        userAdapter.notifyDataSetChanged();
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
            usersDatabaseReference.addChildEventListener(usersChildEventListener);
        }
    }

    private void buildRecyclerView() {
        userRececlyerView=findViewById(R.id.userListRecyclerview);
        userRececlyerView.setHasFixedSize(true);
        userLayoutManager=new LinearLayoutManager(this);
        userAdapter=new UserAdapter(userArrayList);
        userRececlyerView.setLayoutManager(userLayoutManager);
        userRececlyerView.setAdapter(userAdapter);
        userAdapter.setOnUserClickListener(new UserAdapter.O);
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
                startActivity(new Intent(UserListActivity.this,SignInActivity.class));
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
