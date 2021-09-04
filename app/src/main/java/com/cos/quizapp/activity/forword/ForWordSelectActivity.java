package com.cos.quizapp.activity.forword;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.cos.quizapp.activity.MainActivity;

public class ForWordSelectActivity extends BasicActivity {

    private Button btnStepOne, btnStepTwo, btnStepThree, btnStepFour, btnGoMain;
    private int i = 1;
    private String forWordPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forword_select);

        init();
        initData();
    }

    public void init() {
        btnStepOne = findViewById(R.id.btnStepOne);
        btnStepTwo = findViewById(R.id.btnStepTwo);
        btnStepThree = findViewById(R.id.btnStepThree);
        btnStepFour = findViewById(R.id.btnStepFour);
        btnGoMain = findViewById(R.id.btnGoMain);
    }

    public void initData() {
        btnStepOne.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    ForWordActivity.class
            );
            intent.putExtra("forWordPath", "forwordOne");
            startActivity(intent);
        });
        btnStepTwo.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    ForWordActivity.class
            );
            intent.putExtra("forWordPath", "forwordTwo");
            startActivity(intent);
        });
        btnStepThree.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    ForWordActivity.class
            );
            intent.putExtra("forWordPath", "forwordThree");
            startActivity(intent);
        });
        btnStepFour.setOnClickListener(v -> {
            Intent intent = new Intent(
                    this,
                    ForWordActivity.class
            );
            intent.putExtra("forWordPath", "forwordFour");
            startActivity(intent);
        });
        btnGoMain.setOnClickListener(v -> {
            startMyActivity(MainActivity.class);
        });
    }

    private void startMyActivity(Class c){
        Intent intent = new Intent(this, c); // 여기에 넣어주는 클래스의 화면으로 이동함
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
