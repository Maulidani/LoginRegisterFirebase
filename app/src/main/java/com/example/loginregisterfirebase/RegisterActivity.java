package com.example.loginregisterfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    //Deklarasi Variable
    private EditText myEmail, myPassword;
    private Button regButtton;
    private FirebaseAuth auth;
    private String getEmail, getPassword;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Inisialisasi Widget dan Membuat Objek dari Firebae Authenticaion
        myEmail = findViewById(R.id.regEmail);
        myPassword = findViewById(R.id.regPass);
        regButtton = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();

        regButtton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cekDataUser();
            }
        });
    }

    //Method ini digunakan untuk mengecek dan mendapatkan data yang dimasukan user
    private void cekDataUser() {
        //Mendapatkan dat yang diinputkan User
        getEmail = myEmail.getText().toString();
        getPassword = myPassword.getText().toString();

        //Mengecek apakah email dan sandi kosong atau tidak
        if (getEmail.isEmpty() || getPassword.isEmpty() ){
            Toast.makeText(this, "Tidak boleh kosong", Toast.LENGTH_SHORT).show();
        } else {
            loading = ProgressDialog.show(RegisterActivity.this,
                    null,
                    "Please Wait...",
                    true, false);
            createUserAccount();
        }

    }

    //Method ini digunakan untuk membuat akun baru user
    private void createUserAccount() {
        auth.createUserWithEmailAndPassword(getEmail, getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        loading.dismiss();

                        //Mengecek status keberhasilan saat medaftarkan email dan sandi baru
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            //Mengecek panjang karakter password baru yang akan didaftarkan
                            if(getPassword.length() < 6){
                                Toast.makeText(RegisterActivity.this, "Sandi Terlalu Pendek, Minimal 6 Karakter", Toast.LENGTH_SHORT).show();
                            }else {

                                Toast.makeText(RegisterActivity.this, "Terjadi Kesalahan, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }
}