package com.example.awesomechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.UserViewHolder>{

private ArrayList<User> users;
private OnUserClickListener listener;

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }

    public   void  setOnUserClickListener(OnUserClickListener listener){
this.listener=listener;

}
    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
User caurentUser=users.get(position);
holder.avatarImageView.setImageResource(caurentUser.getAvatarMockUpRecourse());
holder.userNameTextView.setText(caurentUser.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static  class UserViewHolder extends RecyclerView.ViewHolder {
        public ImageView avatarImageView;
        public TextView userNameTextView;
        public UserViewHolder(@NonNull View itemView, final OnUserClickListener listener) {
            super(itemView);
            avatarImageView=itemView.findViewById(R.id.avatarImageView);
            userNameTextView=itemView.findViewById(R.id.userNameTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position=getAdapterPosition();
                        if (position !=RecyclerView.NO_POSITION);
                        listener.onUserClick(position);

                    }
                }
            });
        }
    }
}
