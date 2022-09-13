package com.example.insta.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.insta.Adapter.NotificationAdapter;
import com.example.insta.Model.NotificationModel;
import com.example.insta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class NotificationFragment extends Fragment {

    RecyclerView recycler_view;
    ArrayList<NotificationModel> notification_list;
    FirebaseDatabase database;

    public NotificationFragment() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.fragment_notification, container, false);


       recycler_view = view.findViewById(R.id.recycler_view);
       notification_list = new ArrayList<>();
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.profile, R.drawable.hamza, "Alan", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.profile, R.drawable.hamza, "Watson", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));
//       notification_list.add(new NotificationModel(R.drawable.hamza, R.drawable.hamza, "Ali", "Like your Photo"));

        NotificationAdapter notificationAdapter = new NotificationAdapter(notification_list, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view.setLayoutManager(linearLayoutManager);
        recycler_view.setNestedScrollingEnabled(false);
        recycler_view.setAdapter(notificationAdapter);

        database.getReference()
                .child("notification")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notification_list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            NotificationModel notificationModel = dataSnapshot.getValue(NotificationModel.class);
                            notificationModel.setNotificationID(dataSnapshot.getKey());
                            notification_list.add(notificationModel);
                        }
                        notificationAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

       return view;
    }
}