package com.blizzard.app.findmyblood;


import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private RecyclerView requestlist;
    private View v;
    private DatabaseReference dbRef,userRef,typeRef;
    private String current_user;
    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_account2,container,false);

        dbRef= FirebaseDatabase.getInstance().getReference().child("Blood_Request");
        current_user=FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");

        requestlist=(RecyclerView)v.findViewById(R.id.accountfragment_recycler);
        requestlist.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Users> options=new FirebaseRecyclerOptions.Builder<Users>()
                .setQuery(dbRef.child(current_user),Users.class)
                .build();

        FirebaseRecyclerAdapter<Users,RequestListHolder> adapter=new FirebaseRecyclerAdapter<Users, RequestListHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final RequestListHolder holder, int position, @NonNull Users model) {
               //holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
               //holder.fname.setText(model.getFullName());
                //holder.itemView.findViewById(R.id.request_reject_button).setVisibility(View.VISIBLE);

                final String list_user_id=getRef(position).getKey();
                typeRef=getRef(position).child("Request_Type").getRef();
                typeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                           //holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
                            String type=dataSnapshot.getValue().toString();
                            if(type.equals("received")){
                                userRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String request_name = dataSnapshot.child("FullName").getValue().toString();
                                        final String request_location=dataSnapshot.child("Location").getValue().toString();
                                        final String request_mobile=dataSnapshot.child("Phone").getValue().toString();
                                        final String image=dataSnapshot.child("image").getValue().toString();
                                        holder.fname.setText(request_name);
                                        holder.location.setText(request_location);
                                        holder.cancel.setText("Cancel");
                                        holder.mobile.setText("+91 "+request_mobile);
                                        holder.sentImg.setVisibility(View.INVISIBLE);
                                        holder.sentReqImg.setVisibility(View.INVISIBLE);
                                        holder.map.setText("Open Maps");
                                        holder.mapImg.setVisibility(View.VISIBLE);
                                        Picasso.get().load(image).into(holder.profileImage);
                                        holder.cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dbRef.child(current_user).child(list_user_id)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    dbRef.child(list_user_id).child(current_user)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(getContext(),"You have rejected the request.",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        });


                                        holder.mobile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i=new Intent(Intent.ACTION_DIAL);
                                                i.setData(Uri.parse("tel: +91"+request_mobile));
                                                startActivity(i);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                            else if(type.equals("sent")){
                                userRef.child(list_user_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        final String request_name = dataSnapshot.child("FullName").getValue().toString();
                                        final String request_location="You have sent a request to "+request_name;
                                        final String request_mobile=dataSnapshot.child("Phone").getValue().toString();
                                        final String image=dataSnapshot.child("image").getValue().toString();
                                        holder.fname.setText(request_name);
                                        holder.location.setText(request_location);
                                        holder.cancel.setText("Cancel");
                                        holder.locImg.setVisibility(View.INVISIBLE);
                                        holder.mobile.setVisibility(View.INVISIBLE);
                                        holder.reqImg.setVisibility(View.INVISIBLE);
                                        holder.sentImg.setVisibility(View.VISIBLE);
                                        holder.sentReqImg.setVisibility(View.VISIBLE);
                                        holder.map.setVisibility(View.INVISIBLE);
                                        holder.mapImg.setVisibility(View.INVISIBLE);
                                        Picasso.get().load(image).into(holder.profileImage);

                                        holder.cancel.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dbRef.child(current_user).child(list_user_id)
                                                        .removeValue()
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if(task.isSuccessful()){
                                                                    dbRef.child(list_user_id).child(current_user)
                                                                            .removeValue()
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if(task.isSuccessful()){
                                                                                        Toast.makeText(getContext(),"You have Cancelled the request.",Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        });


                                        holder.mobile.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i=new Intent(Intent.ACTION_DIAL);
                                                i.setData(Uri.parse("tel: +91"+request_mobile));
                                                startActivity(i);
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        else{
                            holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.INVISIBLE);
                            holder.itemView.findViewById(R.id.request_accept_reject_mobile).setVisibility(View.INVISIBLE);
                            holder.itemView.findViewById(R.id.request_accept_reject_locImg).setVisibility(View.INVISIBLE);
                            holder.itemView.findViewById(R.id.request_accept_reject_ReqImg).setVisibility(View.INVISIBLE);
                            holder.itemView.findViewById(R.id.request_accept_reject_Cardview).setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"No Requests for Blood",Toast.LENGTH_LONG).show();
                            //holder.itemView.findViewById(R.id.request_accept_button).setVisibility(View.VISIBLE);
                            //Toast.makeText(getContext(),"No Requests for Blood",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
             }

            @NonNull
            @Override
            public RequestListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.request_list_accept_reject,parent,false);
                RequestListHolder requestListHolder=new RequestListHolder(view);
                return requestListHolder;
            }
        };
        requestlist.setAdapter(adapter);
        adapter.startListening();
    }

    public static class RequestListHolder extends RecyclerView.ViewHolder{
        private TextView fname,location,map;
        private Button cancel,mobile;
        private ImageView locImg,reqImg,sentImg,sentReqImg,mapImg;
        private CircleImageView profileImage;
        public RequestListHolder(View itemView) {
            super(itemView);

            fname=(TextView) itemView.findViewById(R.id.request_accept_reject_name);
            cancel=(Button)itemView.findViewById(R.id.request_accept_button);
            location=(TextView)itemView.findViewById(R.id.request_accept_reject_location);
            mobile=(Button)itemView.findViewById(R.id.request_accept_reject_mobile);
            locImg=(ImageView) itemView.findViewById(R.id.request_accept_reject_locImg);
            reqImg=(ImageView)itemView.findViewById(R.id.request_accept_reject_ReqImg);
            sentImg=(ImageView)itemView.findViewById(R.id.request_accept_reject_SentImg);
            sentReqImg=(ImageView)itemView.findViewById(R.id.request_accept_reject_sentreqImg);
            map=(TextView)itemView.findViewById(R.id.request_accept_reject_choosemap);
            mapImg=(ImageView)itemView.findViewById(R.id.request_accept_reject_mapImg);
            profileImage=(CircleImageView)itemView.findViewById(R.id.request_accept_reject_profileimage);
            //reject=(Button)itemView.findViewById(R.id.request_reject_button);
        }
    }
}
