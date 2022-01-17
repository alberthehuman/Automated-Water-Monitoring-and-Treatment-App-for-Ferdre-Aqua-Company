package com.example.waterapp4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;

    private HomeFragment homeFragment;
    private GraphFragment graphFragment;
    private ReportsFragment reportsFragment;
    private SettingsFragment settingsFragment;

    EditText new_email_et,new_pass_et,new_pass2_et;
    Button new_info_but,cancel_but;

    EditText dialog_username_et,dialog_pass_et;
    Button login_but,create_acc_but;

    ConstraintLayout mainlayout;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        frameLayout = findViewById(R.id.frameLayout);

        homeFragment = new HomeFragment();
        graphFragment = new GraphFragment();
        reportsFragment = new ReportsFragment();
        settingsFragment = new SettingsFragment();

        firebaseAuth = FirebaseAuth.getInstance();

        mainlayout = findViewById(R.id.mainlayout_xml);

        if(isConnected()&&firebaseAuth.getCurrentUser()!=null){
            Snackbar.make(mainlayout,"Welcome Back", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        }
        else if (isConnected()&&firebaseAuth.getCurrentUser()==null){
            showLoginDialog();
        }
        else {
            Snackbar.make(mainlayout,"Offline", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Close", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .show();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.navigation_home :
                        initializeFragment(homeFragment);
                        return true;
                    case R.id.navigation_graph :
                        initializeFragment(graphFragment);
                        return true;
                    case R.id.navigation_reports :
                        initializeFragment(reportsFragment);
                        return true;
                    case R.id.navigation_settings :
                        initializeFragment(settingsFragment);
                        return true;
                }
                return false;
            }
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frameLayout, homeFragment);
        ft.commit();
    }

    private void initializeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    private void showLoginDialog(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.login,null);

        dialog_username_et = view.findViewById(R.id.dialog_username_xml);
        dialog_pass_et = view.findViewById(R.id.dialog_pass_xml);
        login_but = view.findViewById(R.id.login_but_xml);
        create_acc_but = view.findViewById(R.id.create_acc_xml);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

        login_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dialog_username_str = dialog_username_et.getText().toString().trim();
                String dialog_pass_str = dialog_pass_et.getText().toString().trim();


                if (dialog_username_str.isEmpty()&&dialog_pass_str.isEmpty()){
                    dialog_username_et.setError("Enter Email");
                    dialog_pass_et.setError("Enter Password");
                }

                else if (dialog_username_str.isEmpty()){
                    dialog_username_et.setError("Enter Email");
                }
                else if (dialog_pass_str.isEmpty()){
                    dialog_pass_et.setError("Enter Password");
                }
                else{

                    firebaseAuth.signInWithEmailAndPassword(dialog_username_str,dialog_pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){ // accepts 6 or more string on password
                                alertDialog.dismiss();
                            } else {
                                dialog_username_et.setError("Invalid Account");
                            }
                        }
                    });
                }
            }
        });

        create_acc_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignUpDialog();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    private void showSignUpDialog(){
        final LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.signup,null);

        new_info_but = view.findViewById(R.id.new_info_xml);
        new_email_et = view.findViewById(R.id.new_email_xml);
        new_pass_et = view.findViewById(R.id.new_pass_xml);
        new_pass2_et = view.findViewById(R.id.new_pass2_xml);
        cancel_but = view.findViewById(R.id.cancel_xml);


        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(false)
                .create();

            new_info_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String new_email_str = new_email_et.getText().toString().trim();
                    String new_pass_str = new_pass_et.getText().toString().trim();
                    String new_pass2_str = new_pass2_et.getText().toString().trim();

                    if(new_email_str.isEmpty()&&new_pass_str.isEmpty()&&new_pass2_str.isEmpty()){
                        new_email_et.setError("Enter Email");
                        new_pass_et.setError("Enter Password");
                        new_pass2_et.setError("Enter Password");
                    }
                    else if (new_email_str.isEmpty()){
                        new_email_et.setError("Enter Email");
                    }
                    else if (new_pass_str.isEmpty()){
                        new_pass_et.setError("Enter Password");
                    }
                    else if (new_pass_str.isEmpty()){
                        new_pass2_et.setError("Enter Password");
                    }
                    else if(new_pass_str.length()<6){
                        new_pass_et.setError("Password must be above 6 characters");
                    }
                    else if (!new_pass_str.equals(new_pass2_str)){
                        new_pass2_et.setError("Password does not match");
                    }
                    else{
                        firebaseAuth.createUserWithEmailAndPassword(new_email_str,new_pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                    new_email_et.setError("User Already Exists");
                                } else if(task.isSuccessful()){
                                    alertDialog.dismiss();
                                    showLoginDialog();
                                }
                            }
                        });
                    }
                }
            });

            cancel_but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLoginDialog();
                    alertDialog.dismiss();
                }
            });


        alertDialog.show();
    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
        }
}
