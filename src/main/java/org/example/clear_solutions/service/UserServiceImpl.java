package org.example.clear_solutions.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.example.clear_solutions.data.UserRepository;
import org.example.clear_solutions.domain.User;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User user, Long userId) {
    return findUserById(userId)
        .map(userInDb -> userInDb.toBuilder()
            .email(user.getEmail())
            .address(user.getAddress())
            .phoneNumber(user.getPhoneNumber())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .birthDate(user.getBirthDate())
            .build())
        .map(userRepository::save)
        .orElseGet(() -> {
          user.setId(userId);
          return userRepository.save(user);
        });
  }

  @Override
  public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
  }


  @Override
  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }

  @Override
  public List<User> findAllByBirthdayBetween(LocalDate from, LocalDate to) {
    return userRepository.findAllByBirthdayBetween(from, to);
  }
}
