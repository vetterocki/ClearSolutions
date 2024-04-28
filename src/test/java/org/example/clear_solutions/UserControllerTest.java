package org.example.clear_solutions;


import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.example.clear_solutions.data.UserRepository;
import org.example.clear_solutions.domain.User;
import org.example.clear_solutions.web.dto.BirthDateRangeRequest;
import org.example.clear_solutions.web.dto.UserModifyDto;
import org.example.clear_solutions.web.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
  private static final String URL = "users";

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private UserRepository userRepository;


  @Test
  @DisplayName("Test user creation when all properties are valid.")
  void testUserCreation() throws Exception {
    UserModifyDto user = userMapper.toDto(createUser());

    String json = objectMapper.writeValueAsString(user);

    mockMvc.perform(post("/{url}", URL)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpectAll(
            jsonPath("$.id", notNullValue()),
            jsonPath("$.email", is("example@example.com")));
  }

  @Test
  @DisplayName("Test user creation when all properties are invalid.")
  void testInvalidUserCreation() throws Exception {
    UserModifyDto user = new UserModifyDto(); // Invalid user

    String json = objectMapper.writeValueAsString(user);
    mockMvc.perform(post("/{url}", URL)
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isBadRequest())
        /* For empty object resulting bad request will contain following:
             [
              "Specify email.",
              "Specify first name.",
              "Specify last name.",
              "Specify birth date."
             ]
           In fact, it is array containing violated validation constraints,
           and, therefore, its size equals 4.
         */
        .andExpect(jsonPath("$", hasSize(4)));
  }

  @Test
  @DisplayName("Test updating an existing user with valid properties.")
  void testUserUpdate() throws Exception {
    User created = userRepository.save(createUser());
    UserModifyDto user = userMapper.toDto(created);
    user.setFirstName("Jane");
    user.setAddress(null);

    String json = objectMapper.writeValueAsString(user);

    mockMvc.perform(put("/{url}/{id}", URL, created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(created.getId().intValue())))
        .andExpect(jsonPath("$.firstName", is("Jane")))
        // PUT updated full object, so 'address' property must be null.
        .andExpect(jsonPath("$.address", is(nullValue())));
  }

  @Test
  @DisplayName("Test partial update of a user with valid properties.")
  void testPartialUserUpdate() throws Exception {
    User created = userRepository.save(createUser());
    UserModifyDto user = new UserModifyDto();
    user.setPhoneNumber("1234567890"); // Modify only the phone number for the patch update

    String json = objectMapper.writeValueAsString(user);

    mockMvc.perform(patch("/{url}/{id}", URL, created.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(created.getId().intValue())))
        .andExpect(jsonPath("$.phoneNumber", is("1234567890")))
        // PATCH updated only specified values in DTO, so some fields contain preivous values.
        .andExpect(jsonPath("$.firstName", is("John")));
  }

  @Test
  @DisplayName("Test deletion of a user.")
  void testUserDeletion() throws Exception {
    User created = userRepository.save(createUser());

    mockMvc.perform(delete("/{url}/{id}", URL, created.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    assertFalse(userRepository.findById(created.getId()).isPresent());
  }

  @Test
  @DisplayName("Test finding users by valid birth date range")
  void testFindUsersByValidBirthDateRange() throws Exception {
    userRepository.save(createUser());
    BirthDateRangeRequest request = new BirthDateRangeRequest();
    request.setFrom(LocalDate.of(2000, 1, 1));
    request.setTo(LocalDate.of(2002, 12, 31));

    mockMvc.perform(get("/{url}", String.format("%s/by-date", URL))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].firstName").exists());
  }

  @Test
  @DisplayName("Test finding users with invalid birth date range")
  void testFindUsersWithInvalidBirthDateRange() throws Exception {
    BirthDateRangeRequest request = new BirthDateRangeRequest();
    request.setFrom(LocalDate.of(2000, 12, 31));
    request.setTo(LocalDate.of(1990, 1, 1));

    mockMvc.perform(get("/{url}", String.format("%s/by-date", URL))
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$.length()").value(1))
        // Due to a fact 'from' is after the 'to' property, validation violation occurs,
        // that`s why resulting JSON would be
        //   [
        //    "Specify correct date range."
        //  ]
        .andExpect(jsonPath("$").value("Specify correct date range."));
  }



  private static User createUser() {
    return User.builder()
        .email("example@example.com")
        .firstName("John")
        .lastName("Doe")
        .birthDate(LocalDate.of(2000, 5, 15))
        .phoneNumber("0974547144")
        .build();
  }

}
