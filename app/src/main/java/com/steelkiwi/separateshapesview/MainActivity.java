package com.steelkiwi.separateshapesview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.steelkiwi.library.SeparateShapesView;

public class MainActivity extends AppCompatActivity implements SeparateShapesView.OnButtonClickListener {

    private SeparateShapesView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (SeparateShapesView) findViewById(R.id.view);
        view.setOnButtonClickListener(this);

    }

    @Override
    public boolean onLeftButtonClick() {
        Toast.makeText(this, "Left button click", Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onRightButtonClick() {
        Toast.makeText(this, "Right button click", Toast.LENGTH_SHORT).show();
        return true;
    }
}
