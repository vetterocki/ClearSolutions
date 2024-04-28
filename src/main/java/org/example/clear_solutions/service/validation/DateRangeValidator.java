package org.example.clear_solutions.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.clear_solutions.web.dto.BirthDateRangeRequest;

public class DateRangeValidator implements ConstraintValidator<IsValidDateRange, BirthDateRangeRequest> {

  @Override
  public boolean isValid(BirthDateRangeRequest request, ConstraintValidatorContext ctx) {
    return request.getFrom().isBefore(request.getTo());
  }
}
