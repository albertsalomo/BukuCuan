package com.betbet.bukucuan.activity.validation;

public class EmailRule extends Rule {
    String Email;

    public EmailRule(String email) {
        Email = email;
    }

    @Override
    public void validate() {
        if (Email.trim().length() == 0){
            IsValid = false;
            ErrorMessage = "Email must not be empty";
            return;
        }

        if (!validateFormat(Email)){
            IsValid = false;
            ErrorMessage = "Email format is incorrect";
            return;
        }

        IsValid = true;
        ErrorMessage = "";
    }

    private boolean validateFormat(String email){
        int firstAtCharIdx = email.indexOf('@');

        if (firstAtCharIdx == -1){
            return false;
        }

        if (email.endsWith("@")){
            return false;
        }

        if (email.startsWith("@") || email.startsWith(".") || email.endsWith(".")){
            return false;
        }

        String afterAtStr = email.substring(firstAtCharIdx + 1);

        if (afterAtStr.startsWith(".") || afterAtStr.contains("@")) {
            return false;
        }
        return afterAtStr.contains(".");
    }
}

