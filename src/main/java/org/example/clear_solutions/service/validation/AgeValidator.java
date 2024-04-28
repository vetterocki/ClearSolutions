package org.example.clear_solutions.service.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;

public class AgeValidator implements ConstraintValidator<IsLegalAge, LocalDate> {
  @Value("${register.required.age}")
  private Integer requiredAge;

  @Override
  public boolean isValid(LocalDate birthDate, ConstraintValidatorContext ctx) {
    LocalDate currentDate = LocalDate.now();
    return Optional.ofNullable(birthDate)
        .map(date -> Period.between(date, currentDate))
        .map(Period::getYears)
        .map(years -> years > requiredAge)
        .orElse(Boolean.TRUE);
  }
}
