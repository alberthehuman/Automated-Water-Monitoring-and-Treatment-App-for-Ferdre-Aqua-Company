package com.example.waterapp4;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class GraphFragment extends Fragment {

    Button g_button;
    String dostr,pHstr;
    SwipeRefreshLayout swipeRefreshLayout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Sensor data");


    public GraphFragment() {
        // Required empty public constructor
    }

    FirebaseDatabase database;
    DatabaseReference reference, reference2;

    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
    GraphView graphView,graphView2;
    LineGraphSeries series;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_graph, container, false);

        swipeRefreshLayout = v.findViewById(R.id.refreshlayout_xml);

        graphView = v.findViewById(R.id.xml_graph);
        graphView2 = v.findViewById(R.id.xml_graph2);

        series = new LineGraphSeries();
        graphView.addSeries(series);
        graphView2.addSeries(series);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("DO_table");
        reference2 = database.getReference("PH_table");

        setListeners();


        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long) value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        graphView2.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX){
                    return sdf.format(new Date((long) value));
                }else{
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        return v;
    }

    private void setListeners() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                collectionReference.orderBy("time", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                                    HomeData homeData = documentSnapshot.toObject(HomeData.class);
                                    homeData.setDO_Home(documentSnapshot.getString("Dissolved_Oxygen"));
                                    homeData.setpH_Home(documentSnapshot.getString("pH_Level"));

                                    dostr = homeData.getDO_Home();
                                    pHstr = homeData.getpH_Home();

                                    String id = reference.push().getKey();
                                    long x = new Date().getTime();
                                    int y = Integer.parseInt(dostr);

                                    String id2 = reference2.push().getKey();
                                    long a = new Date().getTime();
                                    int b = Integer.parseInt(pHstr);

                                    PointValue pointValue = new PointValue(x,y);
                                    PointValue pointValue2 = new PointValue(a,b);

                                    reference.child(id).setValue(pointValue);
                                    reference2.child(id2).setValue(pointValue2);

                                    swipeRefreshLayout.setRefreshing(false);
                                }
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataPoint[] dp = new DataPoint[(int)dataSnapshot.getChildrenCount()];
                int index = 0;

                for(DataSnapshot myDataSnapshot : dataSnapshot.getChildren()){
                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);

                    dp[index] = new DataPoint(pointValue.getXvalue(),pointValue.getYvalue());
                    index++;
                }
                series.resetData(dp);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
