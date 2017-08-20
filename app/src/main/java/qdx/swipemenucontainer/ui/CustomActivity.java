package qdx.swipemenucontainer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import qdx.swipemenucontainer.R;
import qdx.swipemenucontainer.SwipeMenuLayout;

public class CustomActivity extends AppCompatActivity {

    private SwipeMenuLayout swipeLeft;
    private SwipeMenuLayout swipeRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);

        findViewById(R.id.tv_content).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CustomActivity.this, "点击到了内容", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.tv_content).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(CustomActivity.this, "长按事件", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        swipeLeft = (SwipeMenuLayout) findViewById(R.id.swipe_left);
        swipeRight = (SwipeMenuLayout) findViewById(R.id.swipe_right);


    }

    public void btn_del(View view) {
        Toast.makeText(this, "删除", Toast.LENGTH_SHORT).show();
    }

    public void btn_edit(View view) {
        swipeLeft.collapseSmooth();
        swipeRight.collapseSmooth();
        Toast.makeText(this, "编辑    ", Toast.LENGTH_SHORT).show();
    }
}
