package qdx.swipemenucontainer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import qdx.swipemenucontainer.R;
import qdx.swipemenucontainer.data.RecAdapter;

public class RecyclerViewActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecAdapter adapter = new RecAdapter();
        recyclerView.setAdapter(adapter);
    }
}
