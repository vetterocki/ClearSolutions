package org.example.clear_solutions.domain;

public interface EntityMarker<ID> {
  ID getId();

  void setId(ID id);
}
