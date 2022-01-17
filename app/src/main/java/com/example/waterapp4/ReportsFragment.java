package com.example.waterapp4;


import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.StringValue;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {

    Button bt;
//    TextView tv;
    String yeardata;

    private TextView reports_data;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Sensor data");



    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reports, container, false);

        reports_data = v.findViewById(R.id.tv_reports_data_xml);

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bt = view.findViewById(R.id.cal_but_xml);
        //tv = view.findViewById(R.id.show_xml);


        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            yeardata = monthOfYear+1+"-"+dayOfMonth+"-"+year;

            collectionReference.orderBy("forCal", Query.Direction.ASCENDING)
                    //.limit(44)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            String data = "";

                            for (final QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                ReportsData reportsData = documentSnapshot.toObject(ReportsData.class);
                                reportsData.setDO_Report(documentSnapshot.getString("Dissolved_Oxygen"));
                                reportsData.setpH_Report(documentSnapshot.getString("pH_Level"));
                                reportsData.setForCal_Report(documentSnapshot.getString("forCal"));

                                String repdostr = reportsData.getDO_Report();
                                String repphstr = reportsData.getpH_Report();
                                String forcalstr = reportsData.getForCal_Report();


                                if (forcalstr.equals(yeardata)) {
                                    data +="DO: " + repdostr + "\n" + "pH: " + repphstr + "\n\n";
                                }

                                reports_data.setText(yeardata+"\n"+data);
                            }
                        }
                    });

        }
    };

}
