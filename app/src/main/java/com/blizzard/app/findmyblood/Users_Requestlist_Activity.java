package com.blizzard.app.findmyblood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Users_Requestlist_Activity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference dbRef;
    private String uid;
    private ArrayList<Users> list;
    String loc = "";
    String bg = "";
    Users u;
    FirebaseRecyclerAdapter<Users,UserListHolder> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users__requestlist_);

        recyclerView = (RecyclerView) findViewById(R.id.users_requestlist_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<Users>();
        loc = getIntent().getExtras().getString("Location");
        bg = getIntent().getExtras().getString("Bloodgroup");

        dbRef = FirebaseDatabase.getInstance().getReference("Users");

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query query1 = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("Bloodgroup")
                .startAt(bg).endAt(bg);

        Query query2 = FirebaseDatabase.getInstance().getReference("Users")
                .orderByChild("Location")
                .startAt(loc).endAt(loc + "\uf8ff");

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(query2, Users.class).build();


        adapter = new FirebaseRecyclerAdapter<Users, UserListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserListHolder holder, final int position, @NonNull Users model) {

                if(model.getBloodgroup().equals(bg)){
                holder.FullName.setText(model.getFullName());
                holder.Location.setText(model.getLocation());
                holder.Phone.setVisibility(View.INVISIBLE);
                holder.Bloodgroup.setText(model.getBloodgroup());
                Picasso.get().load(model.getImage()).into(holder.profileImage);
                holder.Request.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uid = getRef(position).getKey();
                        Intent i = new Intent(Users_Requestlist_Activity.this, RequestAccountActivity.class);
                        i.putExtra("userid", uid);
                        startActivity(i);
                    }
                });
                    /*holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            uid=getRef(position).getKey();
                            Intent i=new Intent(Users_Requestlist_Activity.this,RequestAccountActivity.class);
                            i.putExtra("userid",uid);
                            startActivity(i);
                        }
                    });*/
                }
                else{
                    holder.requestlist.setVisibility(View.INVISIBLE);
                    Toast.makeText(Users_Requestlist_Activity.this, "Donors Not Available.", Toast.LENGTH_LONG).show();
                }
            }

            @NonNull
            @Override
            public UserListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_listlayout, parent, false);
                UserListHolder userListHolder = new UserListHolder(view);
                return userListHolder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }


    public static class UserListHolder extends RecyclerView.ViewHolder{
        private TextView FullName;
        private TextView Location;
        private TextView Phone;
        private TextView Bloodgroup;
        private Button Request;
        private CardView requestlist;
        private CircleImageView profileImage;
        public UserListHolder(View itemView) {
            super(itemView);
            FullName = (TextView) itemView.findViewById(R.id.user_name);
            Location = (TextView) itemView.findViewById(R.id.user_location);
            Phone = (TextView) itemView.findViewById(R.id.user_phone);
            Bloodgroup = (TextView) itemView.findViewById(R.id.user_bloodgroup);
            Request=(Button)itemView.findViewById(R.id.request_button);
            requestlist=(CardView)itemView.findViewById(R.id.request_listlayout_Cardview);
            profileImage=(CircleImageView)itemView.findViewById(R.id.user_image);
        }
    }
}
