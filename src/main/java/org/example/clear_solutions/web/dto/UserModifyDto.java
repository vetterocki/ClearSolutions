package org.example.clear_solutions.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import lombok.Data;
import org.example.clear_solutions.service.validation.IsLegalAge;
import org.example.clear_solutions.service.validation.PatchConstraint;
import org.example.clear_solutions.service.validation.PutConstraint;

@Data
public class UserModifyDto {

  @NotBlank(message = "Specify email.", groups = PutConstraint.class)
  @Email(message = "Specify email in correct format.", groups = {PutConstraint.class, PatchConstraint.class})
  private String email;

  @NotBlank(message = "Specify first name.", groups = PutConstraint.class)
  private String firstName;

  @NotBlank(message = "Specify last name.", groups = PutConstraint.class)
  private String lastName;

  @NotNull(message = "Specify birth date.", groups = PutConstraint.class)
  @IsLegalAge(message = "Specify correct birth date.", groups = {PutConstraint.class, PatchConstraint.class})
  private LocalDate birthDate;

  private String address;

  // Phone number pattern for flexible format including any country code
  @Pattern(regexp = "^\\+?[0-9]{10,}$", message = "Specify a valid phone number", groups = {PutConstraint.class, PatchConstraint.class})
  private String phoneNumber;
}
