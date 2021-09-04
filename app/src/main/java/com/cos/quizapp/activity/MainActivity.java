package com.cos.quizapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.forword.ForWordSelectActivity;
import com.cos.quizapp.activity.inference.InferenceSelectActivity;
import com.cos.quizapp.activity.knowledge.KnowledgeSelectActivity;
import com.cos.quizapp.activity.user.JoinActivity;
import com.cos.quizapp.activity.user.UserInitActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends CustomAppBarActivity {
    private static final String TAG = "MainActivity2";

    private FirebaseUser user;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onAppBarSetting(true);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(false);

        user = FirebaseAuth.getInstance().getCurrentUser();

        // 현재 로그인된 유저가 있는지 확인, 없으면 회원가입 화면으로 보냄
        if(user == null){
            startMyActivity(JoinActivity.class);
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null){
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                startMyActivity(UserInitActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }
        ranking();
    }

    // 이미지 영역으로 클릭 시 해당 화면으로 전환
    public void forWord(View view){
        startMyActivity(ForWordSelectActivity.class);
    }
    public void conan(View view){
        startMyActivity(InferenceSelectActivity.class);
    }
    public void knowledge(View view){
        startMyActivity(KnowledgeSelectActivity.class);
    }

    // score 별로 ranking 설정
    public void ranking(){
        CollectionReference productRef = database.collection("users");
        productRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<String> arrScore = new ArrayList<>();
                    ArrayList<String> arrRank = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, "document: " + document);
                        arrScore.add(String.valueOf(document.get("score")));
//                        Log.d(TAG, "arrScore: " + arrScore);
                        arrRank.add("1");
//                        Log.d(TAG, "arrRank: " + arrRank);
                    }

                    int[] arrayScore = new int[arrScore.size()];
                    int size1=0;
                    for(String temp : arrScore){
                        arrayScore[size1++] = Integer.parseInt(temp);
                    }

                    int[] arrayRank = new int[arrRank.size()];
                    int size2=0;
                    for(String temp : arrRank){
                        arrayRank[size2++] = Integer.parseInt(temp);
                    }

                    for(int i = 0; i < arrayScore.length; i++) {
                        arrayRank[i] = 1; // 순위 배열을 for 돌때마다 1등으로 초기화
                        for(int j = 0; j < arrayScore.length; j++) { // 배열 i 인덱스의 점수와 나머지 점수 비교
                            if(arrayScore[i] < arrayScore[j]) { // i 인덱스의 값보다 크다면
                                arrayRank[i] = arrayRank[i] + 1; // 순위 증가
                            }
                        }
                    }

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference washingtonRef = database.collection("users").document(document.getId());
                        washingtonRef.update("rank", arrayRank[i]);
                        i++;
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void startMyActivity(Class c){
        Intent intent = new Intent(this, c); // 여기에 넣어주는 클래스의 화면으로 이동함
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}