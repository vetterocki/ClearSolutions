package org.example.clear_solutions.web;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ResponseEntityControllerAdvice {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<List<String>> handleValidationError(MethodArgumentNotValidException exception) {
    if (exception.getGlobalErrors().isEmpty()) {
      return exception.getFieldErrors().stream()
          .filter(fe -> Objects.nonNull(fe.getDefaultMessage()))
          .collect(collectingAndThen(mapping(FieldError::getDefaultMessage, toList()),
              map -> ResponseEntity.badRequest().body(map))
          );
    }
    return exception.getGlobalErrors().stream()
        .filter(Objects::nonNull)
        .map(ObjectError::getDefaultMessage)
        .collect(collectingAndThen(toList(), list -> ResponseEntity.badRequest().body(list)));

  }

}
