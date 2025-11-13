package com.muted987.cloudStorage.config.annotations.validation;

import com.muted987.cloudStorage.dto.response.ValidationResult;
import com.muted987.cloudStorage.service.PathService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;


public class ValidPathValidator implements ConstraintValidator<ValidPath, String> {

    @Autowired
    private PathService pathService;

    @Override
    public boolean isValid(String path, ConstraintValidatorContext context) {
        if (path == null) {
            return false;
        }

        ValidationResult validationResult = pathService.validatePath(path);

        if (!validationResult.valid()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(validationResult.message())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}