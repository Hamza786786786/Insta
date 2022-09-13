package com.example.insta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.Model.FriendsModel;
import com.example.insta.Model.NotificationModel;
import com.example.insta.Model.UsersModel;
import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder>{

    ArrayList<UsersModel> list;
    Context context;

    public UsersAdapter(ArrayList<UsersModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.search_users_sample, parent, false);

        return new UsersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {

        UsersModel usersModelmodel = list.get(position);
        Picasso.get().load(usersModelmodel.getProfile_image()).placeholder(R.drawable.depositphotos).into(holder.search_profile);
        holder.search_users.setText(usersModelmodel.getName());
        holder.search_username.setText(usersModelmodel.getUsername());

        FirebaseDatabase.getInstance().getReference()
                        .child("Users")
                                .child(usersModelmodel.getUserID())
                                        .child("followers")
                                                .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            holder.follow_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_actice_btn));
                            holder.follow_btn.setText("Following");
                            holder.follow_btn.setTextColor(context.getResources().getColor(R.color.black));
                            holder.follow_btn.setEnabled(false);
                        }

                        else {
                            holder.follow_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    FriendsModel friendsModel = new FriendsModel();
                                    friendsModel.setFollowedBy(FirebaseAuth.getInstance().getUid());
                                    friendsModel.setFollowedAt(new Date().getTime());

                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(usersModelmodel.getUserID())
                                            .child("followers")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .setValue(friendsModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    FirebaseDatabase.getInstance().getReference()
                                                            .child("Users")
                                                            .child(usersModelmodel.getUserID())
                                                            .child("followerCount")
                                                            .setValue(usersModelmodel.getFollowerCount() + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    holder.follow_btn.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.follow_actice_btn));
                                                                    holder.follow_btn.setText("Following");
                                                                    holder.follow_btn.setTextColor(context.getResources().getColor(R.color.black));
                                                                    holder.follow_btn.setEnabled(false);
                                                                    Toast.makeText(context, "You Followed "+ usersModelmodel.getName(), Toast.LENGTH_SHORT).show();

                                                                    NotificationModel notificationModel = new NotificationModel();
                                                                    notificationModel.setNotificationBy(FirebaseAuth.getInstance().getUid());
                                                                    notificationModel.setNotificationAt(new Date().getTime());
                                                                    notificationModel.setType("follow");

                                                                    FirebaseDatabase.getInstance().getReference()
                                                                            .child("notification")
                                                                            .child(usersModelmodel.getUserID())
                                                                            .push()
                                                                            .setValue(notificationModel);
                                                                }
                                                            });
                                                }
                                            });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView search_profile;
        TextView search_users, search_username;
        Button follow_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            search_profile = itemView.findViewById(R.id.profileimg_pro);
            search_users = itemView.findViewById(R.id.search_users);
            search_username = itemView.findViewById(R.id.search_username);
            follow_btn = itemView.findViewById(R.id.follow_btn);
        }
    }

}
