package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.entity.GridMember; //
import com.nep.service.GridMemberService; //
import com.nep.service.impl.GridMemberServiceImpl; //

public class NepgLoginActivity extends AppCompatActivity {

    private EditText txt_id;
    private EditText txt_password;
    private GridMemberService gridMemberService;

    public static GridMember currentGridMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nepg_login);

        txt_id = findViewById(R.id.txt_id);
        txt_password = findViewById(R.id.txt_password);
        Button btnLogin = findViewById(R.id.btn_login);

        gridMemberService = new GridMemberServiceImpl(); //

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        if (txt_id.getText().toString().isEmpty()) {
            Toast.makeText(this, "数据格式错误: 登录账号不能为空，请重新输入登录账号", Toast.LENGTH_SHORT).show(); //
            return;
        }
        if (txt_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "数据格式错误: 登录密码不能为空，请重新输入登录密码", Toast.LENGTH_SHORT).show(); //
            return;
        }

        GridMember gm = gridMemberService.login(txt_id.getText().toString(), txt_password.getText().toString()); //
        if (gm != null) {
            currentGridMember = gm;
            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(NepgLoginActivity.this, NepgAqiConfirmActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "登录失败: 登录账号和密码错误，请重新输入账号和密码", Toast.LENGTH_SHORT).show(); //
        }
    }
}
