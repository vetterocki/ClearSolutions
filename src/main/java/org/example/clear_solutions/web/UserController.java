package org.example.clear_solutions.web;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.clear_solutions.domain.User;
import org.example.clear_solutions.service.UserService;
import org.example.clear_solutions.service.validation.PatchConstraint;
import org.example.clear_solutions.service.validation.PutConstraint;
import org.example.clear_solutions.web.dto.BirthDateRangeRequest;
import org.example.clear_solutions.web.dto.UserModifyDto;
import org.example.clear_solutions.web.dto.UserViewDto;
import org.example.clear_solutions.web.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@RestController
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;


  @PostMapping
  public ResponseEntity<UserViewDto> create(@Validated(PutConstraint.class) @RequestBody UserModifyDto userModifyDto) {
    User created = userService.saveUser(userMapper.toEntity(userModifyDto));
    return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(created));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserViewDto> partialUpdate(@PathVariable Long id,
                                                   @Validated(PatchConstraint.class) @RequestBody UserModifyDto modifyDto) {
    return ResponseEntity.of(userService.findUserById(id)
        .map(user -> userMapper.partialUpdate(modifyDto, user))
        .map(userService::saveUser)
        .map(userMapper::toDto));
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserViewDto> fullUpdate(@PathVariable Long id,
                                                @Validated(PutConstraint.class) @RequestBody UserModifyDto modifyDto) {
    User updated = userService.updateUser(userMapper.toEntity(modifyDto), id);
    return ResponseEntity.ok(userMapper.toDto(updated));
  }


  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteById(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/by-date")
  public ResponseEntity<List<UserViewDto>> findAllByBirthdayBetween(@Valid @RequestBody
                                                              BirthDateRangeRequest request) {
    return userService.findAllByBirthdayBetween(request.getFrom(), request.getTo())
        .stream()
        .map(userMapper::toDto)
        .collect(Collectors.collectingAndThen(Collectors.toList(), ResponseEntity::ok));
  }
}
