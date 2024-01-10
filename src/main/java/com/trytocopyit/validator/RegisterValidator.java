package com.trytocopyit.validator;

import com.trytocopyit.entity.Acc;
import com.trytocopyit.form.UserForm;
import com.trytocopyit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RegisterValidator implements Validator {
    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Acc acc = (Acc) target;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.regForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty.regForm.name");

        String username = acc.getUserName();
        if (username != null && username.length() > 0) {
            if (username.matches("\\s+") || !username.matches("[a-zA-Z]+")) {
                errors.rejectValue("pattern", "Pattern.regForm.pattern");
            }else if(userRepository.findAccount(username) != null){
                errors.rejectValue("acc", "Duplicate.regForm.findacc");
            }
        }
    }
}
