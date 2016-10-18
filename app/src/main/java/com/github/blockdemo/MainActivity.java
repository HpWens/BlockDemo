package com.github.blockdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.github.blockdemo.widget.BlockFrameLayout;
import com.github.blockdemo.widget.BlockView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private BlockFrameLayout mBlockFrameLayout;

    private List<Integer> mColors = new ArrayList<>();

    private Random mRandom;

    private int r = 50;
    private int g = 50;
    private int b = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBlockFrameLayout = (BlockFrameLayout) findViewById(R.id.bfl);
        for (int i = 0; i < 25; i++) {
            mRandom = new Random();
            mColors.add(Color.rgb(mRandom.nextInt(255), mRandom.nextInt(255), mRandom.nextInt(255)));
        }
        mBlockFrameLayout.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return mColors.size();
            }

            @Override
            public Object getItem(int position) {
                return mColors.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                BlockView bv = new BlockView(MainActivity.this);
                bv.setPaintColor(mColors.get(position));
                bv.setText("" + position);
                bv.setBlockOnClickListener(new BlockView.OnClickListener() {
                    @Override
                    public void BlockOnClickListener(String text) {
                        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
                return bv;
            }
        });
    }
}
