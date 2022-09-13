package com.example.insta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.insta.Model.FriendsModel;
import com.example.insta.Model.UsersModel;
import com.example.insta.R;
import com.example.insta.databinding.FriendsRvSampleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder>{


    ArrayList<FriendsModel> friendsModelArrayList;
    Context context;

    public FriendAdapter(ArrayList<FriendsModel> friendsModelArrayList, Context context) {
        this.friendsModelArrayList = friendsModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.friends_rv_sample, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FriendsModel friendsModelmodel = friendsModelArrayList.get(position);

        FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(friendsModelmodel.getFollowedBy()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersModel usersModel = snapshot.getValue(UsersModel.class);
                        Picasso.get()
                                .load(usersModel.getProfile_image())
                                .placeholder(R.drawable.depositphotos)
                                .into(holder.binding.searchProfileSample);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    @Override
    public int getItemCount() {
        return friendsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        FriendsRvSampleBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FriendsRvSampleBinding.bind(itemView);

        }
    }

}
