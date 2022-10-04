package com.example.insta.Fragment;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.insta.Adapter.PostAdapter;
import com.example.insta.Adapter.StoryAdapter;
import com.example.insta.Model.PostModel;
import com.example.insta.Model.StoryModel;
import com.example.insta.Model.UsersStories;
import com.example.insta.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;
import com.mikelau.views.shimmer.ShimmerRecyclerView;

import java.util.ArrayList;
import java.util.Date;


public class HomeFragment extends Fragment {

    ShimmerRecyclerView dashboardRV, storyRv;
    ArrayList<StoryModel> storylist;
    ArrayList<PostModel> dashboardlist;
    FirebaseDatabase database;
    FirebaseAuth auth;
    RoundedImageView addStoryImage;
    FirebaseStorage storage;
    ActivityResultLauncher<String> galleryLauncher;
    ProgressDialog dialog;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialog = new ProgressDialog(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dashboardRV = view.findViewById(R.id.dashboardRV);
        dashboardRV.showShimmerAdapter();

        storyRv = view.findViewById(R.id.storyRv);
        storyRv.showShimmerAdapter();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Story Uploading");
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);


        storylist = new ArrayList<>();



        StoryAdapter adapter = new StoryAdapter(storylist, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        storyRv.setLayoutManager(linearLayoutManager);
        storyRv.setNestedScrollingEnabled(false);


        database.getReference()
                .child("stories").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            storylist.clear();
                            for (DataSnapshot storySnapshot : snapshot.getChildren()){
                                StoryModel storyModel = new StoryModel();
                                storyModel.setStoryBy(storySnapshot.getKey());
                                storyModel.setStoryAt(storySnapshot.child("postedBy").getValue(Long.class));

                                ArrayList<UsersStories> usersStories = new ArrayList<>();
                                for (DataSnapshot snapshot1 : storySnapshot.child("userStories").getChildren()){
                                    UsersStories usersStories1 = snapshot1.getValue(UsersStories.class);
                                    usersStories.add(usersStories1);
                                }

                                storyModel.setStories(usersStories);
                                storylist.add(storyModel);

                            }

                            storyRv.setAdapter(adapter);
                            storyRv.hideShimmerAdapter();
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        dashboardlist = new ArrayList<>();


        PostAdapter postAdapter = new PostAdapter(dashboardlist, getContext());
        LinearLayoutManager layoutManager  = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        dashboardRV.setLayoutManager(layoutManager);
        dashboardRV.setNestedScrollingEnabled(false);


        database.getReference().child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dashboardlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PostModel postModel = dataSnapshot.getValue(PostModel.class);
                    postModel.setPostId(dataSnapshot.getKey());
                    dashboardlist.add(postModel);
                }
                dashboardRV.setAdapter(postAdapter);
                dashboardRV.hideShimmerAdapter();
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        addStoryImage = view.findViewById(R.id.story_image);
        addStoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galleryLauncher.launch("image/*");
            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    addStoryImage.setImageURI(result);

                    dialog.show();

                    final StorageReference reference = storage.getReference()
                            .child("stories")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(new Date().getTime() + "");
                    reference.putFile(result).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    StoryModel storyModel = new StoryModel();
                                    storyModel.setStoryAt(new Date().getTime());
                                    database.getReference()
                                            .child("stories")
                                            .child(FirebaseAuth.getInstance().getUid())
                                            .child("postedBy")
                                            .setValue(storyModel.getStoryAt()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    UsersStories usersStories = new UsersStories(uri.toString(), storyModel.getStoryAt());

                                                    database.getReference()
                                                            .child("stories")
                                                            .child(FirebaseAuth.getInstance().getUid())
                                                            .child("userStories")
                                                            .push()
                                                            .setValue(usersStories).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    dialog.dismiss();
                                                                }
                                                            });

                                                }
                                            });
                                }
                            });
                        }
                    });

                }

                else {
                    Toast.makeText(getContext(), "You don't select any story...", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}