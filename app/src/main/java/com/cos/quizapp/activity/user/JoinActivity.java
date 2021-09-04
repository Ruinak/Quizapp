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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class JoinActivity extends BasicActivity {
    private static final String TAG = "JoinActivity";

    private FirebaseAuth mAuth;

    private RelativeLayout layoutLoader;
    private Button btnJoin, btnGoLogin;
    private TextInputEditText etEmail, etPassword, etPasswordCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
        initLr();
    }

    public void init() {
        btnJoin = findViewById(R.id.btnJoin);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etPasswordCheck = findViewById(R.id.etPasswordCheck);
        btnGoLogin = findViewById(R.id.btnGoLogin);
        layoutLoader = findViewById(R.id.layoutLoader);

        mAuth = FirebaseAuth.getInstance();
    }

    private void initLr() {
        btnJoin.setOnClickListener(v -> {
            join();
        });
        btnGoLogin.setOnClickListener(v -> {
            startMyActivity(LoginActivity.class);
        });
    }

    // 회원가입
    private void join() {
        String email = etEmail.getText().toString();
        String password =  etPassword.getText().toString();
        String passwordCheck =  etPasswordCheck.getText().toString();

        // 이메일, 비밀번호, 비밀번호 체크 입력 여부 확인
        if(email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            layoutLoader.setVisibility(View.VISIBLE);
            // 비밀번호와 비밀번호 체크의 일치 여부 확인
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        layoutLoader.setVisibility(View.GONE);
                        // 비밀번호가 일치하는 경우
                        if (task.isSuccessful()) {
                            // 회원가입 성공
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast("성공적으로 회원가입이 되었습니다.");
                            startMyActivity(LoginActivity.class);
                        } else {
                            // 일치하지 않는 경우 회원가입 실패
                            if (task.getException() != null) {
                                Toast(task.getException().toString());
                            }
                        }
                    }
                });
            } else {
                // 비밀번호가 일치하지 않는 경우
                Toast("비밀번호가 일치하지 않습니다.");
            }
        }else {
            // 이메일이나 비밀번호를 입력하지 않은 경우
            Toast("이메일 또는 비밀번호를 입력해주세요.");
        }
    }

    // AVD 뒤로가기를 누르면 앱 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    // startMyActivity 에 넣어주는 클래스의 화면으로 이동함
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