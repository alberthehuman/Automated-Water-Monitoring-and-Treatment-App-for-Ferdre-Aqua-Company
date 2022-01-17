package com.example.waterapp4;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import me.bendik.simplerangeview.SimpleRangeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    SimpleRangeView do_range, ph_range;
    TextView do_tv, ph_tv;
    EditText pass_et;
    Button sett_but,logout_but;
    String do_min, do_max, pH_min, pH_max;
    String user, datetime;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        do_range = v.findViewById(R.id.do_range_xml);
        ph_range = v.findViewById(R.id.ph_range_xml);
        do_tv = v.findViewById(R.id.do_tv_xml);
        ph_tv = v.findViewById(R.id.ph_tv_xml);

        do_range.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                float ifl = i/10f;
                float ill = i1/10f;
                do_tv.setText(String.valueOf(ifl) + " - " + String.valueOf(ill));
                do_min = String.valueOf(ifl);
                do_max = String.valueOf(ill);
            }
        });

        do_range.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                float ifl = i/10f;
                do_tv.setText(String.valueOf(ifl));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                float ifl = i/10f;
                do_tv.setText(String.valueOf(ifl));
            }
        });

        ph_range.setOnChangeRangeListener(new SimpleRangeView.OnChangeRangeListener() {
            @Override
            public void onRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i, int i1) {
                float ifl = i/10f;
                float ill = i1/10f;
                ph_tv.setText(String.valueOf(ifl) + " - " + String.valueOf(ill));
                pH_min = String.valueOf(ifl);
                pH_max = String.valueOf(ill);
            }
        });

        ph_range.setOnTrackRangeListener(new SimpleRangeView.OnTrackRangeListener() {
            @Override
            public void onStartRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                float ifl = i/10f;
                ph_tv.setText(String.valueOf(ifl));
            }

            @Override
            public void onEndRangeChanged(@NotNull SimpleRangeView simpleRangeView, int i) {
                float ifl = i/10f;
                ph_tv.setText(String.valueOf(ifl));
            }
        });
        sett_but = v.findViewById(R.id.validate_xml);
        sett_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (do_min == null || do_max == null || pH_min == null || pH_max == null){
                    showErrorDialog();
                } else {
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                    user = firebaseUser.getUid();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                    datetime = simpleDateFormat.format(calendar.getTime());

                    Map<String, Object> RangeRecords_Map = new HashMap<>();
                    RangeRecords_Map.put("DO Min Range",do_min);
                    RangeRecords_Map.put("DO Max Range",do_max);
                    RangeRecords_Map.put("pH Min Range",pH_min);
                    RangeRecords_Map.put("pH Max Range",pH_max);
                    RangeRecords_Map.put("timestamp", datetime);
                    RangeRecords_Map.put("user", user);

                    Map<String, Object> DO_Map = new HashMap<>();
                    DO_Map.put("DO Min Range",do_min);
                    DO_Map.put("DO Max Range",do_max);

                    Map<String, Object> PH_Map = new HashMap<>();
                    PH_Map.put("pH Min Range",pH_min);
                    PH_Map.put("pH Max Range",pH_max);

                    db.collection("Range Records")
                            .add(RangeRecords_Map);

                    db.collection("Settings")
                            .document("DO Levels")
                            .set(DO_Map);

                    db.collection("Settings")
                            .document("Ph Levels")
                            .set(PH_Map)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    showSuccessDialog();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    showErrorDialog();
                                }
                            });
                }
            }
        });

        logout_but = v.findViewById(R.id.logout_but_xml);

        logout_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                SettingsFragment.this.startActivity(intent);
            }
        });

        return v;
    }

    private void showSuccessDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Success")
                .setMessage("The ranges have been applied.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }

    private void showErrorDialog(){
        new AlertDialog.Builder(getContext())
                .setTitle("Error")
                .setMessage("DO and pH values must be filled out.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }
}
