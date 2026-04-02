package com.example.llmchatbot.ui.signup;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import androidx.appcompat.app.AppCompatActivity;

import com.example.llmchatbot.R;
import com.example.llmchatbot.data.repository.UserRepository;
import com.example.llmchatbot.databinding.ActivitySignupBinding;
import com.example.llmchatbot.ui.threads.ThreadListActivity;
import com.example.llmchatbot.util.SessionManager;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private UserRepository userRepository;
    private SessionManager sessionManager;
    private boolean passwordVisible = false;
    private boolean confirmPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository(this);
        sessionManager = new SessionManager(this);

        setupLiveValidation();
        setupPasswordToggles();
        binding.btnBack.setOnClickListener(v -> finish());
        binding.btnSignUp.setOnClickListener(v -> attemptRegister());
        binding.tvLogin.setOnClickListener(v -> finish());
    }

    private void setupPasswordToggles() {
        binding.btnTogglePassword.setOnClickListener(v -> {
            passwordVisible = !passwordVisible;
            if (passwordVisible) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.btnTogglePassword.setImageResource(android.R.drawable.ic_menu_view);
            }
            binding.etPassword.setSelection(binding.etPassword.getText().length());
        });

        binding.btnToggleConfirmPassword.setOnClickListener(v -> {
            confirmPasswordVisible = !confirmPasswordVisible;
            if (confirmPasswordVisible) {
                binding.etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.btnToggleConfirmPassword.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
            } else {
                binding.etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.btnToggleConfirmPassword.setImageResource(android.R.drawable.ic_menu_view);
            }
            binding.etConfirmPassword.setSelection(binding.etConfirmPassword.getText().length());
        });
    }

    private void setupLiveValidation() {
        binding.etName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString().trim()))
                    binding.tilName.setBackgroundResource(R.drawable.bg_input_field);
            }
        });

        binding.etEmail.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(s.toString().trim()).matches())
                    binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field);
            }
        });

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() >= 6)
                    binding.tilPassword.setBackgroundResource(R.drawable.bg_input_field);
                String confirm = binding.etConfirmPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(confirm) && confirm.equals(s.toString().trim()))
                    binding.tilConfirmPassword.setBackgroundResource(R.drawable.bg_input_field);
            }
        });

        binding.etConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                String password = binding.etPassword.getText().toString().trim();
                if (s.toString().trim().equals(password))
                    binding.tilConfirmPassword.setBackgroundResource(R.drawable.bg_input_field);
            }
        });
    }

    private void attemptRegister() {
        String name = binding.etName.getText().toString().trim();
        String email = binding.etEmail.getText().toString().trim();
        String password = binding.etPassword.getText().toString().trim();
        String confirm = binding.etConfirmPassword.getText().toString().trim();
        boolean valid = true;

        if (TextUtils.isEmpty(name)) {
            binding.tilName.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (TextUtils.isEmpty(email)) {
            binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            binding.tilPassword.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (TextUtils.isEmpty(confirm) || !confirm.equals(password)) {
            binding.tilConfirmPassword.setBackgroundResource(R.drawable.bg_input_field_error);
            valid = false;
        }

        if (!valid) return;

        userRepository.register(name, email, password, id -> runOnUiThread(() -> {
            if (id == -1L) {
                binding.tilEmail.setBackgroundResource(R.drawable.bg_input_field_error);
            } else {
                sessionManager.saveUserId(id);
                startActivity(new Intent(this, ThreadListActivity.class));
                finish();
            }
        }));
    }
}