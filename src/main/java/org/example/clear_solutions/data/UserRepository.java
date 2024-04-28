package org.example.clear_solutions.data;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.example.clear_solutions.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends StubJpaRepository<User> {

  public List<User> findAllByBirthdayBetween(LocalDate from, LocalDate to) {
    return this.entities.stream()
        .filter(user -> isWithinRange(user, from, to))
        .toList();
  }

  private boolean isWithinRange(User user, LocalDate from, LocalDate to) {
    LocalDate target = user.getBirthDate();
    return !(target.isBefore(from) || target.isAfter(to));
  }
}
