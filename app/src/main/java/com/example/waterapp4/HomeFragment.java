package com.example.waterapp4;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private TextView tv_do_data, tv_ph_data, tv_temp_data, tv_lime_data, tv_mix_data, tv_do_limit_data, tv_ph_limit_data;

    private String dostr, pHstr, tempstr, limestr, mixstr, dorangestr, phrangestr;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Sensor data");


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_home, container, false);

        tv_do_data = v.findViewById(R.id.do_data_xml_id);
        tv_ph_data = v.findViewById(R.id.ph_data_xml_id);
        tv_temp_data = v.findViewById(R.id.temp_data_xml_id);
        tv_lime_data = v.findViewById(R.id.lime_data_xml_id);
        tv_mix_data = v.findViewById(R.id.mix_data_xml_id);
        tv_do_limit_data = v.findViewById(R.id.do_limit_data_xml_id);
        tv_ph_limit_data = v.findViewById(R.id.ph_limit_data_xml_id);


        collectionReference.orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                            HomeData homeData = documentSnapshot.toObject(HomeData.class);
//                            HomeData homeData = new HomeData();
                            homeData.setDocumentId(documentSnapshot.getId());
                            homeData.setDO_Home(documentSnapshot.getString("Dissolved_Oxygen"));
                            homeData.setpH_Home(documentSnapshot.getString("pH_Level"));
                            homeData.setTemp_Home(documentSnapshot.getString("Temperature"));
                            homeData.setLime_Home(documentSnapshot.getString("Lime"));
                            homeData.setMix_Home(documentSnapshot.getString("Mixture"));
                            homeData.setDO_Range_Home(documentSnapshot.getString("DO_Range"));
                            homeData.setpH_Range_Home(documentSnapshot.getString("pH_Range"));

                            //String documentId = homeData.getDocumentId();

                            dostr = homeData.getDO_Home();
                            pHstr = homeData.getpH_Home();
                            tempstr = homeData.getTemp_Home();
                            limestr = homeData.getLime_Home();
                            mixstr = homeData.getMix_Home();
                            dorangestr = homeData.getDO_Range_Home();
                            phrangestr = homeData.getpH_Range_Home();




                            tv_do_data.setText(dostr);
                            tv_ph_data.setText(pHstr);
                            tv_temp_data.setText(tempstr);
                            tv_lime_data.setText(limestr);
                            tv_mix_data.setText(mixstr);
                            tv_do_limit_data.setText(dorangestr);
                            tv_ph_limit_data.setText(phrangestr);
                        }
                    }
                });
        return v;
    }
}
