package com.blizzard.app.findmyblood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RequestList extends AppCompatActivity {

    DatabaseReference dbRef;
    RecyclerView recyclerView;
    ArrayList<Users> list;
    RequestListAdapter adapter;
    String loc = "";
    String bg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        loc = getIntent().getExtras().getString("Location");
        System.out.print("----------------" + loc);
        bg = getIntent().getExtras().getString("Bloodgroup");

        recyclerView = (RecyclerView) findViewById(R.id.request_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Users>();

        FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = current_user.getUid();
        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        Query query1 = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("Bloodgroup")
                .startAt(bg).endAt(bg);
        Query query2 = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("Location")
                .startAt(loc).endAt(loc);


        query1.addValueEventListener(valueEventListener);


        /*dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Users u=dataSnapshot1.getValue(Users.class);
                    list.add(u);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            list.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users u = dataSnapshot1.getValue(Users.class);
                    if (u.getLocation().equals(loc)) {
                        list.add(u);
                    }

                }
                adapter = new RequestListAdapter(RequestList.this, list);
                recyclerView.setAdapter(adapter);



            } else {
                Toast.makeText(RequestList.this, "Users not found", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(RequestList.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(RequestList.this, "Error", Toast.LENGTH_SHORT).show();
        }
    };


//----------------------------------------Request Adapter Class-------------------------------------

    class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.UserListHolder> {

        Context context;
        ArrayList<Users> users;
        DatabaseReference dbRef;
        String uid;

        public RequestListAdapter(Context context, ArrayList<Users> users) {
            this.context = context;
            this.users = users;
        }


        @NonNull
        @Override
        public UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            dbRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Uid");
            View view = LayoutInflater.from(context).inflate(R.layout.request_listlayout, parent, false);
            UserListHolder userListHolder = new UserListHolder(view);
            return userListHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull UserListHolder holder, final int position) {
            holder.FullName.setText(users.get(position).getFullName());
            holder.Location.setText(users.get(position).getLocation());
            holder.Age.setText(users.get(position).getAge());
            //holder.Phone.setVisibility(View.INVISIBLE);
            //holder.Phone.setText(users.get(position).getPhone());
            holder.Bloodgroup.setText(users.get(position).getBloodgroup());

            uid=(users.get(position).getUid());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(RequestList.this,RequestAccountActivity.class);
                    i.putExtra("userid",uid);
                    startActivity(i);
                }
            });
        }

        @Override
        public int getItemCount() {
            return users.size();
        }

        class UserListHolder extends RecyclerView.ViewHolder {

            private TextView FullName;
            private TextView Location;
            private TextView Phone;
            private TextView Age;
            private TextView Bloodgroup;
            private Button Request;
            public UserListHolder(View itemView) {
                super(itemView);
                FullName = (TextView) itemView.findViewById(R.id.user_name);
                Location = (TextView) itemView.findViewById(R.id.user_location);
               // Phone = (TextView) itemView.findViewById(R.id.user_phone);
              //  Age = (TextView) itemView.findViewById(R.id.user_age);
                Bloodgroup = (TextView) itemView.findViewById(R.id.user_bloodgroup);
                Request=(Button)itemView.findViewById(R.id.request_button);
            }

        }
    }
}