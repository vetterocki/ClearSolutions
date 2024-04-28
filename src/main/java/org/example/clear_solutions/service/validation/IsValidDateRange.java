package org.example.clear_solutions.service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DateRangeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface IsValidDateRange {
  String message() default "Invalid Date Range";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
