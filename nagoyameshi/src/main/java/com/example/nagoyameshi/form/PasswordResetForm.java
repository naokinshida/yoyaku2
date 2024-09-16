package com.example.nagoyameshi.form;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class PasswordResetForm {
    @NotEmpty
    private String token;

    @NotEmpty
    @Size(min = 6, max = 20, message = "パスワードは6文字以上20文字以下で入力してください。")
    private String password;
}
