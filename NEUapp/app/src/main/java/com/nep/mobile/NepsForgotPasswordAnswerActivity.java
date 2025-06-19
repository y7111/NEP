package com.nep.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.nep.entity.Supervisor; //
import com.nep.service.SupervisorService; //
import com.nep.service.impl.SupervisorServiceImpl; //

public class NepsForgotPasswordAnswerActivity extends AppCompatActivity {

    private TextView lbl_securityQuestion;
    private EditText txt_securityAnswer;
    private Supervisor supervisor;
    private SupervisorService supervisorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neps_forgot_password_answer);

        lbl_securityQuestion = findViewById(R.id.lbl_securityQuestion);
        txt_securityAnswer = findViewById(R.id.txt_securityAnswer);
        Button btnSubmitAnswer = findViewById(R.id.btn_submit_answer);
        Button btnBack = findViewById(R.id.btn_back);

        supervisorService = new SupervisorServiceImpl();

        // 从 Intent 中获取 Supervisor 的 loginCode，然后重新加载 Supervisor 对象
        String supervisorLoginCode = getIntent().getStringExtra("supervisorLoginCode");
        if (supervisorLoginCode != null) {
            supervisor = supervisorService.getSupervisorByLoginCode(supervisorLoginCode); //
        }

        if (supervisor != null) {
            lbl_securityQuestion.setText(supervisor.getSecurityQuestion()); // // Use the corrected getter
        } else {
            Toast.makeText(this, "无法加载用户信息，请重试", Toast.LENGTH_SHORT).show();
            finish(); // 结束当前Activity
            return;
        }

        btnSubmitAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAnswer();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    private void submitAnswer() {
        String answer = txt_securityAnswer.getText().toString().trim();
        if (answer.isEmpty()) {
            Toast.makeText(this, "答案不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

        if (supervisor != null && answer.equals(supervisor.getSecurityAnswer())) { // // Use the corrected getter
            Intent intent = new Intent(NepsForgotPasswordAnswerActivity.this, NepsForgotPasswordResetActivity.class);
            intent.putExtra("supervisorLoginCode", supervisor.getLoginCode());
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "答案错误: 密保问题答案错误，请重新输入答案", Toast.LENGTH_SHORT).show(); //
        }
    }

    private void back() {
        Intent intent = new Intent(NepsForgotPasswordAnswerActivity.this, NepsForgotPasswordPhoneActivity.class);
        startActivity(intent);
        finish();
    }
}
