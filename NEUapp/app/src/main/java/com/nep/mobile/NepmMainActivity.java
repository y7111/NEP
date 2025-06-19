package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class NepmMainActivity extends AppCompatActivity {

    private ImageView txt_imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepm_main);

        txt_imageView = findViewById(R.id.txt_imageView);
        // ImageView的图片已经在XML中设置，这里无需额外代码设置图片源
        // 如果需要动态加载，可以使用 Glide/Picasso 等库，或者 BitmapFactory.decodeResource

        Button btnAqiInfo = findViewById(R.id.btn_aqi_info);
        Button btnAqiAssign = findViewById(R.id.btn_aqi_assign);
        Button btnAqiConfirm = findViewById(R.id.btn_aqi_confirm);

        btnAqiInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aqiInfo();
            }
        });

        btnAqiAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aqiAssign();
            }
        });

        btnAqiConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aqiConfirm();
            }
        });
    }

    private void aqiInfo() {
        Intent intent = new Intent(NepmMainActivity.this, NepmAqiInfoActivity.class);
        startActivity(intent);
    }

    private void aqiAssign() {
        Intent intent = new Intent(NepmMainActivity.this, NepmAqiAssignActivity.class);
        startActivity(intent);
    }

    private void aqiConfirm() {
        Intent intent = new Intent(NepmMainActivity.this, NepmConfirmInfoActivity.class);
        startActivity(intent);
    }
}
