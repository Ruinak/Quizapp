package com.cos.quizapp.activity.inference;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.cos.quizapp.activity.GradeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class InferenceActivity extends BasicActivity {
    private static final String TAG = "InferenceActivity";

    private TextView tvQuiz, tvContent, tvSolution;
    private Button btnNext, btnOut, btnSubmit;
    private FirebaseFirestore database;
    private String answer, solution, bScore, facInferencePath;
    private ScrollView scrollView;
    private TextInputEditText etAnswer;
    private int index = 1, grade = 0, score;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inference);

        init();
        initData();
        baseScore();
        initLr();
        grade();
    }

    public void init() {
        tvQuiz = findViewById(R.id.tvQuiz);
        tvContent = findViewById(R.id.tvContent);
        btnNext = findViewById(R.id.btnNext);
        btnOut= findViewById(R.id.btnOut);
        btnSubmit= findViewById(R.id.btnSubmit);
        scrollView = findViewById(R.id.scrollView);
        etAnswer = findViewById(R.id.etAnswer);
        tvSolution = findViewById(R.id.tvSolution);
        Intent selectIntent = getIntent();
        facInferencePath = selectIntent.getStringExtra("inferencePath");
    }

    public void initData() {
        Log.d(TAG, "initData: ??????");
        database = FirebaseFirestore.getInstance();

        inferenceReuse(1);
    }
    public void initLr(){
        btnNext.setOnClickListener(v -> {
            tvQuiz.setTextColor(Color.BLACK);
            etAnswer.setVisibility(View.VISIBLE);
            tvSolution.setVisibility(View.INVISIBLE);
            tvContent.setTextColor(Color.BLACK);
            btnSubmit.setEnabled(true);
            index++;
            inferenceReuse(index);
            scrollView.scrollTo(0,scrollView.getTop());

            if(index == 6) {
                score = Integer.parseInt(bScore) + grade;

                DocumentReference washingtonRef = database.collection("users").document(user.getUid());
                washingtonRef.update("score", score);
                Intent intent = new Intent(
                        this,
                        GradeActivity.class
                );
                intent.putExtra("grade", grade);
                startActivity(intent);
            }
        });

        btnOut.setOnClickListener(v -> {
            startMyActivity(InferenceSelectActivity.class);
        });

        btnSubmit.setOnClickListener(v -> {
            tvContent.setText(solution);
            tvSolution.setVisibility(View.VISIBLE);
            scrollView.scrollTo(0,scrollView.getTop());
            etAnswer.setVisibility(View.INVISIBLE);
            btnSubmit.setEnabled(false);

            if(answer.equals(etAnswer.getText().toString())){
                tvQuiz.setText("???????????????!!");
                tvQuiz.setTextColor(Color.GREEN);
                grade += 20;
            }else {
                tvQuiz.setText("???????????????");
                tvQuiz.setTextColor(Color.RED);
            }

            etAnswer.setText("");
            etAnswer.clearFocus();
            scrollView.scrollTo(0, scrollView.getTop());
        });
    }

    // ?????? ????????? DB ?????? ????????? bScore ??? ??????
    public void baseScore() {
        DocumentReference docRef = database.collection("users").document(user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        bScore = document.get("score").toString();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    // DB ?????? ????????? ?????? ??? ????????? ??????. ?????? / ?????? ?????? ??? ????????? ?????? ??????
    public void inferenceReuse(int i) {
        DocumentReference docRef = database.collection("inference").document(facInferencePath + index);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String s1 = document.get("text").toString().replace("m", "\n\n");
                        tvQuiz.setText((String)document.get("quiz"));
                        tvContent.setText(s1);
                        answer = document.get("answer").toString().trim();
                        solution = document.get("solution").toString().replace("m", "\n\n");
                        scrollView.scrollTo(0,scrollView.getTop());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public int grade() {
        return grade;
    }

    private void startMyActivity(Class c){
        Intent intent = new Intent(this, c); // ????????? ???????????? ???????????? ???????????? ?????????
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}