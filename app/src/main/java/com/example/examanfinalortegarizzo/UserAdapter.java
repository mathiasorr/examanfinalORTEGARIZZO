package com.example.examanfinalortegarizzo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> users;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(User user);
    }

    public UserAdapter(List<User> users, OnItemClickListener listener) {
        this.users = users;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user, listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameTextView;
        TextView countryTextView;
        TextView emailTextView;

        UserViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.userImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            countryTextView = itemView.findViewById(R.id.countryTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }

        void bind(final User user, final OnItemClickListener listener) {
            nameTextView.setText(user.name.first + " " + user.name.last);
            countryTextView.setText(user.location.country);
            emailTextView.setText(user.email);

            Glide.with(imageView.getContext())
                    .load(user.picture.thumbnail)
                    .circleCrop()
                    .into(imageView);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(user);
                }
            });
        }
    }
}
