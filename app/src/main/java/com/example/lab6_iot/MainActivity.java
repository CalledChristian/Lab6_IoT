package com.example.lab6_iot;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.lab6_iot.Adapter.ListaIngresosAdapter;
import com.example.lab6_iot.Bean.Ingreso;
import com.example.lab6_iot.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private FirebaseAuth auth;

    private List<Ingreso> listaIngresos;

    private ListaIngresosAdapter adapter;

    private LinearLayoutManager layoutManager;


    private RecyclerView recyclerView;


    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();


        binding.bottomNavigation.setOnItemReselectedListener( item -> {
            int menuItemId =  item.getItemId();

            if (menuItemId ==  R.id.navigation_ingresos) {
                loadFragment(new IngresosFragment());

            }
            else if (menuItemId ==  R.id.navigation_egresos) {
                loadFragment(new EgresosFragment());

            }
            else if (menuItemId ==  R.id.navigation_resumen) {
                loadFragment(new EgresosFragment());

            }
            else if (menuItemId ==  R.id.navigation_logout) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();

            }

        });

        // Load default fragment
        if (savedInstanceState == null) {
            binding.bottomNavigation.setSelectedItemId(R.id.navigation_ingresos);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}



    /*private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) { //user logged-in
            if (currentUser.isEmailVerified()) {
                Log.d("Firebase uid: ", currentUser.getUid());
                goToAppActivity();
            }
        }

        binding.buttonIngreso.setOnClickListener(view -> {

            binding.buttonIngreso.setEnabled(false);

            AuthMethodPickerLayout authMethodPickerLayout = new AuthMethodPickerLayout.Builder(R.layout.activity_main)
                    .setGoogleButtonId(R.id.btn_login_google)
                    .setEmailButtonId(R.id.btn_login_mail)
                    .build();


            Intent intent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    //.setTheme(R.style.Base_Theme_Clase10)
                    .setIsSmartLockEnabled(false)
                    //.setAuthMethodPickerLayout(authMethodPickerLayout)
                    //.setLogo(R.drawable.pucp)
                    .setAvailableProviders(Arrays.asList(
                            new AuthUI.IdpConfig.EmailBuilder().build(),
                            new AuthUI.IdpConfig.GoogleBuilder().build()
                    ))
                    .build();

            signInLauncher.launch(intent);
        });
    }

        ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
                new FirebaseAuthUIActivityResultContract(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("Firebase uid: " ,user.getUid());
                            /*Log.d("Display name: " ,user.getDisplayName());
                            Log.d("Email: " , user.getEmail());


                            user.reload().addOnCompleteListener(task -> {
                                if (user.isEmailVerified()) {
                                    goToAppActivity();
                                } else {
                                    user.sendEmailVerification().addOnCompleteListener(task2 -> {
                                        Toast.makeText(MainActivity.this, "Se le envió un correo para validar su cuenta", Toast.LENGTH_LONG).show();
                                        //
                                    });
                                }
                            });
                        } else {
                            Log.d("msg-test", "user == null");
                        }
                    } else {
                        Log.d("msg-test", "Canceló el Log-in");
                    }
                    binding.buttonIngreso.setEnabled(true);
                }
        );

    public void goToAppActivity() {
        Intent intent = new Intent(MainActivity.this, AppActivity.class);
        startActivity(intent);
        finish();
    }*/


