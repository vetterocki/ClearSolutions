package org.example.clear_solutions.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.clear_solutions.domain.User;

public interface UserService {
  User saveUser(User user);

  User updateUser(User user, Long userId);

  Optional<User> findUserById(Long id);


  void deleteUser(Long id);

  List<User> findAllByBirthdayBetween(LocalDate from, LocalDate to);
}
