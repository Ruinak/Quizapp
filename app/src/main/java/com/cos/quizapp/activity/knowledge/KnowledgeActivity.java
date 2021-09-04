package com.cos.quizapp.activity.knowledge;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.BasicActivity;
import com.cos.quizapp.activity.GradeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class KnowledgeActivity extends BasicActivity {
    private static final String TAG = "KnowledgeActivity";

    private TextView tvQuiz;
    private Button btnIndex0, btnIndex1, btnIndex2, btnIndex3, btnNext, btnOut;
    private FirebaseFirestore database;
    private String answer, bScore, facKnowledgePath;
    private int index = 1, grade = 0, score;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        init();
        initData();
        baseScore();
        initLr();
        grade();
    }
    public void init() {
        tvQuiz = findViewById(R.id.tvQuiz);
        btnIndex0 = findViewById(R.id.btnIndex0);
        btnIndex1 = findViewById(R.id.btnIndex1);
        btnIndex2 = findViewById(R.id.btnIndex2);
        btnIndex3 = findViewById(R.id.btnIndex3);
        btnNext = findViewById(R.id.btnNext);
        btnOut = findViewById(R.id.btnOut);
        Intent selectIntent = getIntent();
        facKnowledgePath = selectIntent.getStringExtra("knowledgePath");
    }

    public void initData() {
        database = FirebaseFirestore.getInstance();
        knowledgeReuse(1);
    }

    public void initLr(){
        btnNext.setOnClickListener(v -> {
            tvQuiz.setTextColor(Color.BLACK);
            btnIndex0.setTextColor(Color.BLACK);
            btnIndex0.setEnabled(true);
            btnIndex1.setTextColor(Color.BLACK);
            btnIndex1.setEnabled(true);
            btnIndex2.setTextColor(Color.BLACK);
            btnIndex2.setEnabled(true);
            btnIndex3.setTextColor(Color.BLACK);
            btnIndex3.setEnabled(true);
            index++;
            knowledgeReuse(index);

            if(index == 11) {
                score = Integer.parseInt(bScore) + grade;

                DocumentReference washingtonRef = database.collection("users").document(user.getUid());
                washingtonRef.update("score", score);
                Intent intent = new Intent(
                        this,
                        GradeActivity.class
                );
                intent.putExtra("grade",grade);
                startActivity(intent);
            }
        });

        btnOut.setOnClickListener(v -> {
            startMyActivity(KnowledgeSelectActivity.class);
        });
    }

    // 기존 점수를 DB 에서 꺼내고 bScore 에 저장
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

    // DB 에서 문제를 꺼낸 후 화면에 출력. 정답 / 오답 체크 및 정답시 점수 증가
    public void knowledgeReuse(int i) {
        DocumentReference docRef = database.collection("knowledge").document(facKnowledgePath + index);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String[] s1 = document.get("text").toString().split(",");
                        tvQuiz.setText((String)document.get("quiz"));
                        s1[0] = s1[0].replace("[","").trim();
                        s1[1] = s1[1].trim();
                        s1[2] = s1[2].trim();
                        s1[3] = s1[3].replace("]","").trim();
                        btnIndex0.setText(s1[0]);
                        btnIndex1.setText(s1[1]);
                        btnIndex2.setText(s1[2]);
                        btnIndex3.setText(s1[3]);
                        answer = document.get("answer").toString();

                        if(answer.equals(s1[0])){
                            btnIndex0.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.BLUE);
                                btnIndex0.setTextColor(Color.BLUE);
                                btnIndex1.setTextColor(Color.RED);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setTextColor(Color.RED);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setTextColor(Color.RED);
                                btnIndex3.setEnabled(false);
                                grade += 10;
                            });
                        } else if(!answer.equals(s1[0])){
                            btnIndex0.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.RED);
                                btnIndex0.setTextColor(Color.RED);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setEnabled(false);
                            });
                        }

                        if(answer.equals(s1[1])){
                            btnIndex1.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.BLUE);
                                btnIndex0.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setTextColor(Color.BLUE);
                                btnIndex2.setTextColor(Color.RED);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setTextColor(Color.RED);
                                btnIndex3.setEnabled(false);
                                grade += 10;
                            });
                        } else {
                            btnIndex1.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setTextColor(Color.RED);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setEnabled(false);
                            });
                        }

                        if(answer.equals(s1[2])) {
                            btnIndex2.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.BLUE);
                                btnIndex0.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setTextColor(Color.RED);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setTextColor(Color.BLUE);
                                btnIndex3.setTextColor(Color.RED);
                                btnIndex3.setEnabled(false);
                                grade += 10;
                            });
                        } else {
                            btnIndex2.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setTextColor(Color.RED);
                                btnIndex3.setEnabled(false);
                            });
                        }

                        if(answer.equals(s1[3])) {
                            btnIndex3.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.BLUE);
                                btnIndex0.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setTextColor(Color.RED);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setTextColor(Color.RED);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setTextColor(Color.BLUE);
                                grade += 10;
                            });
                        } else {
                            btnIndex3.setOnClickListener(v -> {
                                tvQuiz.setTextColor(Color.RED);
                                btnIndex0.setEnabled(false);
                                btnIndex1.setEnabled(false);
                                btnIndex2.setEnabled(false);
                                btnIndex3.setTextColor(Color.RED);
                            });
                        }
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
        Intent intent = new Intent(this, c); // 여기에 넣어주는 클래스의 화면으로 이동함
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}