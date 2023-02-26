package com.example.userapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.userapp.R;
import com.example.userapp.model.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_details, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.fNameTextView.setText(user.getFirstName());
        holder.lNameTextView.setText(user.getLastName());
        holder.ageTextView.setText(user.getAge());
        holder.emailTextView.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView fNameTextView, lNameTextView, ageTextView, emailTextView;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            fNameTextView = itemView.findViewById(R.id.tvFName);
            lNameTextView = itemView.findViewById(R.id.tvLName);
            ageTextView = itemView.findViewById(R.id.tvAge);
            emailTextView = itemView.findViewById(R.id.tvEmail);
        }
    }

}
