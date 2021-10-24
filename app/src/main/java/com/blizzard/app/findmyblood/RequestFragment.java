package com.blizzard.app.findmyblood;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequestFragment extends Fragment implements View.OnClickListener{

    private Button request_button;
    private Spinner request_spinner;
    private ArrayAdapter<CharSequence> adapter;
    private AutoCompleteTextView autoCompleteTextView;
    private String bloodgroup="",location="";
    private TextView request_heading;

    String[] cities={"Mumbai","Bangalore","Chennai","Kolkata","Delhi","Thane","Pune","Bombay"};


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v= inflater.inflate(R.layout.fragment_request2, container, false);

        request_heading=(TextView)v.findViewById(R.id.request_heading);
        request_heading.setText("Find A Donor");
        request_spinner=(Spinner)v.findViewById(R.id.request_spinner);
        adapter=ArrayAdapter.createFromResource(getContext(),R.array.blood_groups,android.R.layout.simple_spinner_item);
        request_spinner.setSelection(0);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        request_spinner.setAdapter(adapter);
        request_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgroup=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        autoCompleteTextView=(AutoCompleteTextView)v.findViewById(R.id.request_location);
        autoCompleteTextView.getText().toString();
        ArrayAdapter ad=new ArrayAdapter(getContext(),android.R.layout.select_dialog_item,cities);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(ad);

        request_button=(Button)v.findViewById(R.id.request_button);
        request_button.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        if(v==request_button){
            if(!autoCompleteTextView.getText().toString().equals("") && !request_spinner.getSelectedItem().toString().equals("Select Blood Group")) {

                Intent i = new Intent(getActivity(), Users_Requestlist_Activity.class);
                i.putExtra("Fragment", "RequestFragment");
                i.putExtra("Bloodgroup", request_spinner.getSelectedItem().toString());
                i.putExtra("Location", autoCompleteTextView.getText().toString());
                startActivity(i);
            }
            else if(request_spinner.getSelectedItem().toString().equals("Select Blood Group")){
                Toast.makeText(getActivity(),"Select Blood Group",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),"Select Location",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
