package com.cos.quizapp.activity.user;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.cos.quizapp.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends BasicActivity {
    private static final String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    private RelativeLayout layoutLoader;
    private Button btnLogin,btnGoPasswordReset;
    private TextInputEditText etEmail,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        initLr();
    }

    public void init() {
        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnGoPasswordReset = findViewById(R.id.btnGoPasswordReset);
        layoutLoader = findViewById(R.id.layoutLoader);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initLr() {
        btnLogin.setOnClickListener(v -> {
            Login();
        });
        btnGoPasswordReset.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    PasswordResetActivity.class
            );
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    // 로그인
    private void Login() {
        String email = etEmail.getText().toString();
        String password =  etPassword.getText().toString();

        // 이메일이나 비밀번호 입력 여부 확인
        if(email.length() > 0 && password.length() > 0 ) {
            layoutLoader.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    layoutLoader.setVisibility(View.GONE);
                    // 일치하는 경우 로그인 성공
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast("성공적으로 로그인이 되었습니다.");
                        startMyActivity(MainActivity.class);
                    } else {
                        // 일치하지 않는 경우 로그인 실패
                        Toast(task.getException().toString());
                    }
                }
            });
        }else {
            // 이메일이나 비밀번호를 입력하지 않은 경우
            Toast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    private void startMyActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Toast 띄우기
    private void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}