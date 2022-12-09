package com.example.insta.Fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.insta.Adapter.FriendAdapter;
import com.example.insta.Model.FriendsModel;
import com.example.insta.Model.UsersModel;
import com.example.insta.R;
import com.example.insta.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {

    RecyclerView friends_rv;
    ArrayList<FriendsModel> friendsModelArrayList;
    ImageView changecoverpic, cover_photo, profile_imagepro, setting_pro;
    View view;
    FirebaseAuth auth;
    FirebaseStorage storage;
    FirebaseDatabase firebaseDatabase;
    TextView usernamepro, namepro, count_follower_pro;


    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        usernamepro = view.findViewById(R.id.usernamepro);
        namepro = view.findViewById(R.id.namepro);
        profile_imagepro = view.findViewById(R.id.profileimg_pro);
        setting_pro = view.findViewById(R.id.setting_pro);
        count_follower_pro = view.findViewById(R.id.count_followers_pro);

        firebaseDatabase.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UsersModel usersModel = snapshot.getValue(UsersModel.class);
                    Picasso.get().load(usersModel.getCoverPhoto()).placeholder(R.drawable.depositphotos).into(cover_photo);
                    Picasso.get().load(usersModel.getProfile_image()).placeholder(R.drawable.depositphotos).into(profile_imagepro);
                    usernamepro.setText(usersModel.getUsername());
                    namepro.setText(usersModel.getName());
                    count_follower_pro.setText(usersModel.getFollowerCount()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        friends_rv = view.findViewById(R.id.friends_rv);
        friendsModelArrayList = new ArrayList<>();

        FriendAdapter friendAdapter = new FriendAdapter(friendsModelArrayList, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        friends_rv.setLayoutManager(linearLayoutManager);
        friends_rv.setNestedScrollingEnabled(false);
        friends_rv.setAdapter(friendAdapter);

        firebaseDatabase.getReference().child("Users")
                .child(auth.getUid())
                .child("followers").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        friendsModelArrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            FriendsModel friendsModel = dataSnapshot.getValue(FriendsModel.class);
                            friendsModelArrayList.add(friendsModel);

                        }

                        friendAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        changecoverpic = view.findViewById(R.id.changecoverpic_pro);
        cover_photo = view.findViewById(R.id.cover_photo);

        changecoverpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 11);
            }
        });

        profile_imagepro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 22);
            }
        });


        setting_pro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                Intent intent = new Intent(getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 11){
            if(data!=null && resultCode == RESULT_OK){
                Uri uri = data.getData();
                cover_photo.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("cover_photo")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(getContext(), "Cover Photo Saved", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("coverPhoto").setValue(uri.toString());
                            }
                        });
                    }
                });

            }

            else {
                Toast.makeText(getContext(), "You don't select your cover photo...", Toast.LENGTH_LONG).show();
            }

        }

        else if (requestCode == 22){
            if(data!=null && resultCode == RESULT_OK){
                Uri uri = data.getData();
                profile_imagepro.setImageURI(uri);

                final StorageReference reference = storage.getReference().child("profile_image")
                        .child(FirebaseAuth.getInstance().getUid());
                reference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        Toast.makeText(getContext(), "Profile Image Saved", Toast.LENGTH_SHORT).show();
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                firebaseDatabase.getReference().child("Users").child(auth.getUid()).child("profile_image").setValue(uri.toString());
                            }
                        });
                    }
                });

            }

            else {

                Toast.makeText(getContext(), "You don't select your profile image...", Toast.LENGTH_LONG).show();

            }



        }





    }
}