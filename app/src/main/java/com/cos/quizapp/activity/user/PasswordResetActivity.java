package com.cos.quizapp.activity.user;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordResetActivity extends BasicActivity {
    private static final String TAG = "PasswordReset";

    private FirebaseAuth mAuth;

    private TextInputEditText etEmail;
    private Button btnSend, btnGoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        init();
        initLr();
    }

    public void init() {
        btnSend = findViewById(R.id.btnSend);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        etEmail = findViewById(R.id.etEmail);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initLr() {
        btnSend.setOnClickListener(v -> {
            Log.d(TAG, "initData: 클릭됨");
            Send();
        });
        btnGoLogin.setOnClickListener(v -> {
            startMyActivity(LoginActivity.class);
        });
    }

    private void Send() {
        String email = etEmail.getText().toString();

        if(email.length() > 0 ) {
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast("이메일을 보냈습니다.");
                    }
                }
            });
        }else {
            Toast("이메일을 입력해주세요.");
        }
    }

    private void startMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Toast 띄우기
    private void Toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}