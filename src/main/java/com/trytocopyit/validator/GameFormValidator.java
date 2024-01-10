package com.trytocopyit.validator;

import com.trytocopyit.entity.Game;
import com.trytocopyit.form.GameForm;
import com.trytocopyit.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class GameFormValidator implements Validator {

    @Autowired
    private GameRepository gameRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == GameForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        GameForm gameForm = (GameForm) target;


        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "code", "NotEmpty.gameForm.code");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.gameForm.name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "price", "NotEmpty.gameForm.price");

        String code = gameForm.getCode();
        if (code != null && code.length() > 0) {
            if (code.matches("\\s+")) {
                errors.rejectValue("code", "Pattern.gameForm.code");
            } else if (gameForm.isNewGame()) {
                Game game = gameRepository.findGame(code);
                if (game != null) {
                    errors.rejectValue("code", "Duplicate.gameForm.code");
                }
            }
        }
    }

}