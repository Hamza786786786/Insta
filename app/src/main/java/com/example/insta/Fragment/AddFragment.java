package com.example.insta.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.insta.Model.PostModel;
import com.example.insta.Model.UsersModel;
import com.example.insta.R;
import com.example.insta.databinding.FragmentAddBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;


public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    Uri uri;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;
    ProgressDialog dialog;

    public AddFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Post Uploading");
        dialog.setMessage("Please Wait...");
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        database.getReference().child("Users")
                        .child(FirebaseAuth.getInstance().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UsersModel usersModel = snapshot.getValue(UsersModel.class);
                            Picasso.get()
                                    .load(usersModel.getProfile_image())
                                    .placeholder(R.drawable.depositphotos)
                                    .into(binding.profileimgPost);
                            binding.postUsers.setText(usersModel.getName());
                            binding.postUsername.setText(usersModel.getUsername());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.postDescriptionPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String description = binding.postDescriptionPost.getText().toString();
                if(!description.isEmpty()){
                    binding.postBtnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn_bg));
                    binding.postBtnPost.setTextColor(getContext().getResources().getColor(R.color.white));
                    binding.postBtnPost.setEnabled(true);
                }
                else {
                    binding.postBtnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_actice_btn));
                    binding.postBtnPost.setTextColor(getContext().getResources().getColor(R.color.black));
                    binding.postBtnPost.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.addImgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i , "Select") , 10);
            }
        });

        binding.postBtnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();

                final StorageReference reference = storage.getReference().child("posts")
                        .child(FirebaseAuth.getInstance().getUid())
                        .child(new Date().getTime()+"");
                reference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                PostModel postModel = new PostModel();
                                postModel.setPostImage(uri.toString());
                                postModel.setPostedBy(FirebaseAuth.getInstance().getUid());
                                postModel.setPostDescription(binding.postDescriptionPost.getText().toString());
                                postModel.setPostedAt(new Date().getTime());

                                database.getReference().child("posts")
                                        .push()
                                        .setValue(postModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                dialog.dismiss();
                                                Toast.makeText(getContext(), "Posted Successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 10 && resultCode == RESULT_OK && data!=null){
            uri = data.getData();
            binding.postAImgPost.setImageURI(uri);
            binding.postAImgPost.setVisibility(View.VISIBLE);

            binding.postBtnPost.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.follow_btn_bg));
            binding.postBtnPost.setTextColor(getContext().getResources().getColor(R.color.white));
            binding.postBtnPost.setEnabled(true);
        }
        else {
            Toast.makeText(getContext(), "You don't select any image...", Toast.LENGTH_SHORT).show();
        }
    }
}