package com.cos.quizapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cos.quizapp.R;

public class GradeActivity extends BasicActivity {
    private static final String TAG = "GradeActivity";

    private TextView tvGrade;
    private Button btnGoMain;

    private int Grade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade);

        init();
        initData();
        initLr();
    }

    public void init() {
        tvGrade = findViewById(R.id.tvGrade);

        Intent gradeIntent = getIntent();
        Grade = gradeIntent.getIntExtra("grade",1);
        btnGoMain = findViewById(R.id.btnGoMain);
    }

    public void initLr() {
        tvGrade.setText("당신의 점수는 \n" + Grade + "점 입니다!!");
        btnGoMain.setOnClickListener(v ->{
            startMyActivity(MainActivity.class);
        });
    }

    public void initData() {
        Log.d(TAG, "grade: "+ Grade);
    }

    private void startMyActivity(Class c){
        Intent intent = new Intent(this, c); // 여기에 넣어주는 클래스의 화면으로 이동함
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}