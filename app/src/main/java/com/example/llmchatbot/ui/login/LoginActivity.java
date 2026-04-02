package com.example.llmchatbot.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.llmchatbot.R;
import com.example.llmchatbot.data.repository.UserRepository;
import com.example.llmchatbot.databinding.ActivityLoginBinding;
import com.example.llmchatbot.ui.signup.SignUpActivity;
import com.example.llmchatbot.ui.threads.ThreadListActivity;
import com.example.llmchatbot.util.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private boolean passwordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            goToThreadList();
            return;
        }

        setupLiveValidation();
        setupPasswordToggle();
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.tvSignUp.setOnClickListener(v ->
                startActivity(new Intent(this, SignUpActivity.class)));
    }

    private void setupPasswordToggle() {
        binding.btnTogglePassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
            }
            // keep cursor at end after transformation change
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });
    }

    private void setupLiveValidation() {
        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches()) {
                    binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field);
                }
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim())) {
                    binding.tilPassword.setBackgroundResource(R.drawable.bg_input_field);
                }
            }
        });
    }

    private void attemptLogin() {
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (TextUtils.isEmpty(password)) {
            binding.tilPassword.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (!valid) return;

        userRepository.login(email, password, user -> runOnUiThread(() -> {
            if (user != null) {
                sessionManager.saveUserId(user.id);
                goToThreadList();
            } else {
                binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
                binding.tilPassword.setBackgroundResource(R.drawable.bg_input_field_error);
            }
        }));
    }

    private void goToThreadList() {
        startActivity(new Intent(this, ThreadListActivity.class));
        finish();
    }
}