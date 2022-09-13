package com.example.insta.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.CommentActivity;
import com.example.insta.Model.NotificationModel;
import com.example.insta.Model.PostModel;
import com.example.insta.Model.UsersModel;
import com.example.insta.R;
import com.example.insta.databinding.NotificationItemBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    ArrayList<NotificationModel> list;
    Context context;

    public NotificationAdapter(ArrayList<NotificationModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);

        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        NotificationModel model = list.get(position);

        String type = model.getType();

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(model.getNotificationBy())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersModel usersModel = snapshot.getValue(UsersModel.class);
                        Picasso.get()
                                .load(usersModel.getProfile_image())
                                .placeholder(R.drawable.depositphotos)
                                .into(holder.binding.imageProfile);

                        if (type.equals("like")){
                            holder.binding.userName.setText(usersModel.getName());
                            holder.binding.type.setText("Like your post");
                        }
                        else if (type.equals("comment")){
                            holder.binding.userName.setText(usersModel.getName());
                            holder.binding.type.setText("Commented on your post");
                        }

                        else {
                            holder.binding.userName.setText(usersModel.getName());
                            holder.binding.type.setText("Started following you");
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        holder.binding.openNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!type.equals("follow")){

                    FirebaseDatabase.getInstance().getReference()
                                    .child("notification")
                                            .child(model.getPostedBy())
                                                    .child(model.getNotificationID())
                                                            .child("checkOpen")
                                                                    .setValue(true);

                    holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    Intent intent = new Intent(context, CommentActivity.class);
                    intent.putExtra("postId", model.getPostID());
                    intent.putExtra("postedBy", model.getPostedBy());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }

            }
        });

        Boolean checkOpen = model.isCheckOpen();

        if (checkOpen == true){
            holder.binding.openNotification.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else {

        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{


        NotificationItemBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = NotificationItemBinding.bind(itemView);


        }
    }

}
