package com.blizzard.app.findmyblood;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAccountActivity extends AppCompatActivity {

    private TextView displayname;
    private TextView displayaddress;
    private TextView displaybloodgroup;
    private TextView displaylocation;
    private DatabaseReference dbRef,dbReq,notRef;
    private FirebaseUser current_user;
    private String uid;
    private Button requestblood;
    private String current_state="";
    private String req_type;
    private CircleImageView profileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_account);

        displayname=(TextView)findViewById(R.id.requestaccount_name);
        displayaddress=(TextView)findViewById(R.id.requestaccount_address);
        displaybloodgroup=(TextView)findViewById(R.id.requestaccount_bloodgroup);
        uid=getIntent().getStringExtra("userid");
        displayname=(TextView)findViewById(R.id.requestaccount_name);
        displaylocation=(TextView)findViewById(R.id.requestaccount_location);
        profileImage=(CircleImageView) findViewById(R.id.requestaccount_profileimage);



        current_state="not_connected";

        requestblood=(Button)findViewById(R.id.requestaccount_button);

        dbRef= FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
        dbReq=FirebaseDatabase.getInstance().getReference().child("Blood_Request");
        notRef=FirebaseDatabase.getInstance().getReference().child("Notifications");
        current_user= FirebaseAuth.getInstance().getCurrentUser();


         dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String display_name=dataSnapshot.child("FullName").getValue().toString();
                String display_address=dataSnapshot.child("Address").getValue().toString();
                String display_bloodgroup=dataSnapshot.child("Bloodgroup").getValue().toString();
                String display_location=dataSnapshot.child("Location").getValue().toString();
                String display_image=dataSnapshot.child("image").getValue().toString();

                displayname.setText(display_name);
                displayaddress.setText(display_address);
                displaybloodgroup.setText(display_bloodgroup);
                displaylocation.setText(display_location);
                Picasso.get().load(display_image).into(profileImage);

//---------------------Request list-------//

                dbReq.child(current_user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(uid)){
                            req_type=dataSnapshot.child(uid).child("Request_Type").getValue().toString();
                            if(req_type.equals("received")){
                                current_state="req_received";
                                requestblood.setText("Accept Request");
                            }
                            else if(req_type.equals("sent")){
                                current_state="req_sent";
                                requestblood.setText("Cancle Request");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

         requestblood.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 requestblood.setEnabled(false);
//--------IF Not Connected/not friends state------//
                if(current_state.equals("not_connected")){
                    dbReq.child(current_user.getUid()).child(uid).child("Request_Type").setValue("sent").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dbReq.child(uid).child(current_user.getUid()).child("Request_Type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        HashMap<String,String> notificationMap=new HashMap<>();
                                        notificationMap.put("From",current_user.getUid());
                                        notificationMap.put("Type","request");

                                        notRef.child(uid).push().setValue(notificationMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    requestblood.setEnabled(true);
                                                    current_state="req_sent";
                                                    requestblood.setText("Cancel Request ");
                                                    Toast.makeText(RequestAccountActivity.this,"Blood Request Sent", Toast.LENGTH_SHORT).show();
                                                }
                                             }
                                        });


                                    }
                                });
                            }
                            else {
                                Toast.makeText(RequestAccountActivity.this,"Blood Request Sent Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
//--------Cancel Request------------//

                if(current_state.equals("req_sent")){
                    dbReq.child(current_user.getUid()).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            dbReq.child(uid).child(current_user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    requestblood.setEnabled(true);
                                    current_state="not_connected";
                                    requestblood.setText("Request");
                                }
                            });
                        }
                    });
                }


             }

         });
    }
}
