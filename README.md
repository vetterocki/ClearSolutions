# RESTful API Implementation with Spring Boot

This repository contains the implementation of a RESTful API for managing user resources using Spring Boot. The API follows best practices for RESTful design and includes error handling, validation, and unit tests.

## Requirements

The API fulfills the following requirements:

1. **Fields**:
    - Email (required) with validation against email pattern.
    - First name (required).
    - Last name (required).
    - Birth date (required) with validation against the current date.
    - Address (optional).
    - Phone number (optional).

2. **Functionality**:
    - Create user: Allows registration of users who are more than 18 years old.
    - Update user fields: Both partial and full updates are supported.
    - Delete user.
    - Search for users by birth date range, ensuring "From" is less than "To".

3. **Testing**: Unit tests are implemented using Spring framework.

4. **Error Handling**: Error handling for RESTful responses is included.

5. **JSON Format**: API responses are in JSON format.

6. **Database**: Data persistence layer is not implemented; hence, no database usage.

7. **Framework**: Any version of Spring Boot can be used along with the Java version of your choice.

