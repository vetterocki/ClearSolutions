package org.example.clear_solutions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.example.clear_solutions.data.UserRepository;
import org.example.clear_solutions.domain.User;
import org.example.clear_solutions.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;


  @Test
  public void testSaveUser() {
    User user = new User(null, "example@example.com", "John", "Doe", LocalDate.of(2000, 5, 15),
        "123 Main St", "0974547144");

    when(userRepository.save(any(User.class))).thenReturn(user);

    User savedUser = userService.saveUser(user);

    assertNotNull(savedUser);
    assertEquals("example@example.com", savedUser.getEmail());
    assertEquals("John", savedUser.getFirstName());
    verify(userRepository, times(1)).save(user);
  }

  @Test
  public void testUpdateUser() {
    Long userId = 1L;
    User existingUser =
        new User(userId, "old@example.com", "Jane", "Smith", LocalDate.of(1995, 10, 20),
            "456 Elm St", "0987654321");
    User updatedUser =
        new User(userId, "new@example.com", "Alice", "Johnson", LocalDate.of(1990, 3, 12),
            "789 Oak St", "0123456789");

    when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
    when(userRepository.save(any(User.class))).thenReturn(updatedUser);

    User result = userService.updateUser(updatedUser, userId);
    assertEquals(existingUser.getId(), result.getId());
    assertEquals("new@example.com", result.getEmail());
    assertEquals("Alice", result.getFirstName());
    verify(userRepository, times(1)).findById(userId);
    verify(userRepository, times(1)).save(updatedUser);
  }

  @Test
  public void testFindUserById() {
    Long userId = 1L;
    User user = new User(userId, "example@example.com", "John", "Doe", LocalDate.of(2000, 5, 15),
        "123 Main St", "0974547144");

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    Optional<User> foundUser = userService.findUserById(userId);

    assertTrue(foundUser.isPresent());
    assertEquals(user.getEmail(), foundUser.get().getEmail());
    assertEquals(user.getFirstName(), foundUser.get().getFirstName());
  }

  @Test
  public void testDeleteUser() {
    Long userId = 1L;

    userService.deleteUser(userId);

    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  public void testFindAllByBirthdayBetween() {
    LocalDate from = LocalDate.of(1990, 1, 1);
    LocalDate to = LocalDate.of(1995, 12, 31);

    User user1 = new User(null, "user1@example.com", "Alex", "Smith", LocalDate.of(1992, 4, 10),
        "123 Main St", "0123456789");

    User user2 = new User(null, "user2@example.com", "Emma", "Johnson", LocalDate.of(1994, 9, 20),
        "456 Elm St", "0987654321");

    when(userRepository.findAllByBirthdayBetween(from, to)).thenReturn(Arrays.asList(user1, user2));

    List<User> users = userService.findAllByBirthdayBetween(from, to);

    assertEquals(2, users.size());
    assertTrue(users.contains(user1));
    assertTrue(users.contains(user2));
  }

}
