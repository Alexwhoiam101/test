package com.trytocopyit.validator;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.entity.Game;
import com.trytocopyit.form.CustomerForm;
import com.trytocopyit.form.GameForm;
import com.trytocopyit.form.UserForm;
import com.trytocopyit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserFormValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.userForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.userForm.name");

        String username = userForm.getUsername();
        if (username != null && username.length() > 0) {
            if (username.matches("\\s+") || !username.matches("[a-zA-Z0-9]+")) {
                errors.rejectValue("username", "Pattern.userForm.pattern");
            }
        }
    }
}
