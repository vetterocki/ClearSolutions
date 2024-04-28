package org.example.clear_solutions.web.dto;

import java.time.LocalDate;
import lombok.Data;
import org.example.clear_solutions.service.validation.IsValidDateRange;

@Data
@IsValidDateRange(message = "Specify correct date range.")
public class BirthDateRangeRequest {
  private LocalDate from;
  private LocalDate to;
}
