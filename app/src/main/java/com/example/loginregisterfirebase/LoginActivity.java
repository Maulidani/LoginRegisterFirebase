package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    //Deklarasi Variable
    private Button Register, Login;
    private EditText myEmail, myPassword;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;
    private String getEmail, getPassword;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Inisialisasi Widget
        myEmail = findViewById(R.id.email);
        myPassword = findViewById(R.id.pass);
        Register = findViewById(R.id.register);
        Login = findViewById(R.id.login);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));

            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mendapatkan dat yang diinputkan User
                getEmail = myEmail.getText().toString();
                getPassword = myPassword.getText().toString();

                //Mengecek apakah email dan sandi kosong atau tidak
                if(getEmail.isEmpty() || getPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Email atau Sandi Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    loading = ProgressDialog.show(LoginActivity.this,
                            null,
                            "Please Wait...",
                            true, false);
                    loginUserAccount();

                }
            }
        });

        //Instance / Membuat Objek Firebase Authentication
        auth = FirebaseAuth.getInstance();

        //Mengecek Keberadaan User
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //Mengecek apakah ada user yang sudah login / belum logout
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    //Jika ada, maka halaman akan langsung berpidah pada MainActivity
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }
            }
        };
    }

    //Menerapkan Listener
    @Override
    protected void onStart() {
        super.onStart();
        auth.removeAuthStateListener(listener);
//        auth.addAuthStateListener(listener);

    }

    //Melepaskan Litener
    @Override
    protected void onStop() {
        super.onStop();
        if(listener != null){
            auth.removeAuthStateListener(listener);
        }
    }

    //Method ini digunakan untuk proses autentikasi user menggunakan email dan kata sandi
    private void loginUserAccount(){
        auth.signInWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loading.dismiss();

                        //Mengecek status keberhasilan saat login
                        if(task.isSuccessful()){
                            auth.addAuthStateListener(listener);
                            Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "Tidak Dapat Masuk, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}