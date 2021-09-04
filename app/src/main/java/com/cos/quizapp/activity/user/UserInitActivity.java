package com.cos.quizapp.activity.user;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cos.quizapp.Dto.UserInfo;
import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserInitActivity extends BasicActivity {
    private static final String TAG = "UserInitActivity";

    private RelativeLayout layoutLoader;

    private Button btnCheck;
    private TextInputEditText etName, etPhoneNumber, etBirthday, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_init);

        init();
        initLr();
    }

    public void init() {
        btnCheck = findViewById(R.id.btnCheck);
        etName = findViewById(R.id.etName);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBirthday = findViewById(R.id.etBirthday);
        etAddress = findViewById(R.id.etAddress);
        layoutLoader = findViewById(R.id.layoutLoader);
    }

    private void initLr() {
        btnCheck.setOnClickListener(v -> {
            profileUpdate();
        });
    }

    // 내 정보 입력
    private void profileUpdate() {
        String name = etName.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();
        String birthDay = etBirthday.getText().toString();
        String address = etAddress.getText().toString();
        int rank = 0;
        int score = 0;

        // 이메일, 비밀번호, 생일, 주소 입력 여부 확인
        if(name.length() > 0 && phoneNumber.length() > 9 && birthDay.length() > 5 && address.length() >0) {
            layoutLoader.setVisibility(View.VISIBLE);
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            UserInfo userInfo = new UserInfo(name, phoneNumber, birthDay, address, rank, score);

            // user 가 null 이 아닌 경우
            if(user != null) {
                // 회원정보 등록 성공
                db.collection("users").document(user.getUid()).set(userInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast("회원정보 등록을 성공하였습니다.");
                        layoutLoader.setVisibility(View.GONE);
                        finish();
                    }
                })
                // 회원 정보 등록 실패
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast("회원정보 등록에 실패하였습니다.");
                        layoutLoader.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
            }
        }else {
            // 이메일이나 비밀번호를 입력하지 않은 경우
            Toast("회원정보를 입력해주세요.");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    // Toast 띄우기
    private void Toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}