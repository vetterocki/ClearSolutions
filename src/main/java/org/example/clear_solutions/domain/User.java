package org.example.clear_solutions.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements EntityMarker<Long> {
  private Long id;

  private String email;

  private String firstName;

  private String lastName;

  private LocalDate birthDate;

  private String address;

  private String phoneNumber;
}
