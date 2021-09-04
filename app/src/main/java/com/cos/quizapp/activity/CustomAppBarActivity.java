package com.cos.quizapp.activity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cos.quizapp.R;
import com.cos.quizapp.activity.user.UserInfoActivity;
import com.cos.quizapp.activity.user.UserListActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CustomAppBarActivity extends AppCompatActivity {
    private static final String TAG = "CustomAppBarActivity";

    protected void onAppBarSetting(boolean isBackButton) {
        androidx.appcompat.widget.Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setBackgroundColor(Color.rgb(136, 234, 240));
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(isBackButton);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //toolbar 의 back 키 눌렀을 때 동작
                startMyActivity(MainActivity.class);
                return true;
            case R.id.actionRankView:
                startMyActivity(UserListActivity.class);
                return true;
            case R.id.actionLogout:
                FirebaseAuth.getInstance().signOut();
                startMyActivity(MainActivity.class);
                return true;
            case R.id.actionMyView:
                startMyActivity(UserInfoActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startMyActivity(Class c) {
        Intent intent = new Intent(this,c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
