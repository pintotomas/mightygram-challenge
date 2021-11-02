package com.validations;

import java.util.Arrays;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.validations.annotations.ValidFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/** Constraint validator class for ValidFile annotation. */
@Slf4j
public class FileConstraintValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    private ValidFile field;

    @Override
    public void initialize(ValidFile arg) {
        this.field = arg;
    }

    @Override
    public boolean isValid(
            MultipartFile file, ConstraintValidatorContext constraintValidatorContext) {
        if (file == null) {
            log.info("File is null");
            return Boolean.TRUE;
        }
        log.info("Validating file: " + file.getOriginalFilename());
        log.info("Allowed file types: " + Arrays.toString(field.type()));
        log.info("File type: " + file.getContentType());
        return Arrays.stream(field.type())
                .anyMatch(type -> type.getMediaType().equals(file.getContentType()));
    }
}