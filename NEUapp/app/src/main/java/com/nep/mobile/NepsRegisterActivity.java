package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.entity.Supervisor; //
import com.nep.entity.securityQuestion; //
import com.nep.io.FileIO; //
import com.nep.service.SupervisorService; //
import com.nep.service.impl.SupervisorServiceImpl; //

import java.util.ArrayList;
import java.util.List;

public class NepsRegisterActivity extends AppCompatActivity {

    private EditText txt_id;
    private EditText txt_password;
    private EditText txt_repassword;
    private EditText txt_realName;
    private RadioGroup rg_sex;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private Spinner txt_securityQuestion;
    private EditText txt_securityAnswer;

    private SupervisorService supervisorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_register);

        txt_id = findViewById(R.id.txt_id);
        txt_password = findViewById(R.id.txt_password);
        txt_repassword = findViewById(R.id.txt_repassword);
        txt_realName = findViewById(R.id.txt_realName);
        rg_sex = findViewById(R.id.rg_sex);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);
        txt_securityQuestion = findViewById(R.id.txt_securityQuestion);
        txt_securityAnswer = findViewById(R.id.txt_securityAnswer);
        Button btnRegister = findViewById(R.id.btn_register);
        Button btnBack = findViewById(R.id.btn_back);

        supervisorService = new SupervisorServiceImpl();

        // 初始化密保问题下拉列表
        List<securityQuestion> elist = (List<securityQuestion>) FileIO.readObject("securityQuestion.txt"); //
        List<String> questions = new ArrayList<>();
        if (elist != null) {
            for (securityQuestion sq : elist) {
                questions.add(sq.getQuestion()); //
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txt_securityQuestion.setAdapter(adapter);

        // 设置默认选中项
        if (questions.isEmpty()) {
            questions.add("无密保问题"); // If no questions in file, add a default
            txt_securityQuestion.setSelection(0);
        } else {
            txt_securityQuestion.setSelection(0); // Select the first item by default
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void register() {
        String loginCode = txt_id.getText().toString().trim();
        String password = txt_password.getText().toString().trim();
        String rePassword = txt_repassword.getText().toString().trim();
        String realName = txt_realName.getText().toString().trim();
        String sex = ((RadioButton) findViewById(rg_sex.getCheckedRadioButtonId())).getText().toString();
        String securityQuestion = txt_securityQuestion.getSelectedItem().toString();
        String securityAnswer = txt_securityAnswer.getText().toString().trim();

        if (loginCode.isEmpty()) {
            Toast.makeText(this, "用户名为空，请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(rePassword)) {
            Toast.makeText(this, "注册失败: 两次输入密码不一致，请重新输入确认密码", Toast.LENGTH_SHORT).show();
            txt_repassword.setText("");
            return;
        }
        if (realName.isEmpty()) {
            Toast.makeText(this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (securityQuestion.equals("选择密保问题") || securityQuestion.equals("无密保问题")) {
            Toast.makeText(this, "请选择一个密保问题", Toast.LENGTH_SHORT).show();
            return;
        }
        if (securityAnswer.isEmpty()) {
            Toast.makeText(this, "密保问题答案不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Supervisor supervisor = new Supervisor(); //
        supervisor.setLoginCode(loginCode); //
        supervisor.setPassword(password); //
        supervisor.setRealName(realName); //
        supervisor.setSex(sex); //
        supervisor.setSecurityQuestion(securityQuestion); //
        supervisor.setSecurityAnswer(securityAnswer); //


        boolean flag = supervisorService.register(supervisor); //
        if (flag) {
            Toast.makeText(this, loginCode + " 账号注册成功! 可以进行用户登录!", Toast.LENGTH_LONG).show(); //
            Intent intent = new Intent(NepsRegisterActivity.this, NepsLoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "注册失败: 手机号已被注册，请重新输入注册手机号码", Toast.LENGTH_SHORT).show(); //
            txt_id.setText("");
        }
    }

    private void back() {
        Intent intent = new Intent(NepsRegisterActivity.this, NepsLoginActivity.class);
        startActivity(intent);
        finish();
    }
}
