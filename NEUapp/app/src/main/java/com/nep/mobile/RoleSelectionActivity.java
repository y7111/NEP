package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        Button btnPublicSupervisor = findViewById(R.id.btn_public_supervisor);
        Button btnGridMember = findViewById(R.id.btn_grid_member);
        Button btnAdmin = findViewById(R.id.btn_admin);

        btnPublicSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动公众监督员登录界面
                Intent intent = new Intent(RoleSelectionActivity.this, NepsLoginActivity.class);
                startActivity(intent);
            }
        });

        btnGridMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动网格员登录界面
                Intent intent = new Intent(RoleSelectionActivity.this, NepgLoginActivity.class);
                startActivity(intent);
            }
        });

        btnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 启动管理员登录界面
                Intent intent = new Intent(RoleSelectionActivity.this, NepmLoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
