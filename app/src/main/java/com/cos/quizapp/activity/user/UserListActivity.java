package com.cos.quizapp.activity.user;

import android.os.Bundle;
import com.cos.quizapp.R;
import com.cos.quizapp.activity.CustomAppBarActivity;
import com.cos.quizapp.fragment.UserListFragment;

public class UserListActivity extends CustomAppBarActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        onAppBarSetting(true);

        // UserListFragment 를 받아서 화면에 출력
        UserListFragment userListFragment = new UserListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, userListFragment)
                .commit();
    }
}
