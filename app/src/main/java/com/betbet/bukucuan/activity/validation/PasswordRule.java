package com.betbet.bukucuan.activity.validation;


public class PasswordRule extends Rule {
    String Password;

    public PasswordRule(String password) {
        Password = password;
    }

    @Override
    public void validate() {
        int passwordLength = Password.length();
        if (passwordLength < 8){
            IsValid = false;
            ErrorMessage = "Password must have at least 8 characters";
            return;
        }

        // Alphanumeric check
        boolean hasNumeric = false;
        boolean hasAlphabet = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < passwordLength; i++) {
            char character = Password.charAt(i);
            int ascii = (int) character;
            if (ascii >= 48 && ascii <= 57) {
                hasNumeric = true;
            }
            else if ((ascii >= 65 && ascii <= 90) || (ascii >= 97 && ascii <= 122)){
                hasAlphabet = true;
            }
            else {
                hasSpecialCharacter = true;
            }
        }

        if (!hasNumeric || !hasAlphabet || hasSpecialCharacter){
            IsValid = false;
            ErrorMessage = "Password must contain both letters and numbers";
            return;
        }

        IsValid = true;
        ErrorMessage = "";
    }
}

