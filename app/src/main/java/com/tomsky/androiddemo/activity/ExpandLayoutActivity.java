package com.tomsky.androiddemo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tomsky.androiddemo.R;
import com.tomsky.androiddemo.widget.expand.TagExpandableLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzhitao on 2020/09/22
 **/
public class ExpandLayoutActivity extends AppCompatActivity {

    private TagExpandableLayout expandableLayout;
    private Button btn;
    private TextView textView;

    private boolean expand = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand_layout);

        expandableLayout = findViewById(R.id.expand_layout);
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            tags.add("Tag "+i);
        }
        expandableLayout.addTags(tags, tags.get(1));

        btn = findViewById(R.id.expand_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand) {
                    expandableLayout.setMinLine(2);
                    btn.setText("展开");
                } else {
                    expandableLayout.setMinLine(-1);
                    btn.setText("收起");
                }
                expandableLayout.requestLayout();
                expand = !expand;
            }
        });

        textView = findViewById(R.id.tag_text);

        findViewById(R.id.get_tag_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(expandableLayout.getSelectTag());
            }
        });
    }
}
