package org.example.clear_solutions.web.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserViewDto extends UserModifyDto {
  private Long id;
}
