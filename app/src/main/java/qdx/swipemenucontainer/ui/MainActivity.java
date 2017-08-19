package qdx.swipemenucontainer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import qdx.swipemenucontainer.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btn_menu(View view) {
        startActivity(new Intent(MainActivity.this, CustomActivity.class));
    }

    public void btn_rec_menu(View view) {
        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
    }


}
