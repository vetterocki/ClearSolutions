package org.example.clear_solutions.web.mapper;

import org.example.clear_solutions.domain.User;
import org.example.clear_solutions.web.dto.UserModifyDto;
import org.example.clear_solutions.web.dto.UserViewDto;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
  User toEntity(UserModifyDto userModifyDto);

  UserViewDto toDto(User user);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  User partialUpdate(UserModifyDto userModifyDto, @MappingTarget User user);
}
