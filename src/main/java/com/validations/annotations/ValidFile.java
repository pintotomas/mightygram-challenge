package com.validations.annotations;

import com.validations.FileConstraintValidator;
import com.validations.enums.FileType;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

/** Annotation for file type validation. */
@Documented
@Constraint(validatedBy = FileConstraintValidator.class)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidFile {

    String message() default "Invalid file type";

    FileType[] type() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}