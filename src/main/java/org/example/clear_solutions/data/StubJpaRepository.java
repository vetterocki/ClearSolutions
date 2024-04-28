package org.example.clear_solutions.data;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.example.clear_solutions.domain.EntityMarker;


public abstract class StubJpaRepository<E extends EntityMarker<Long>> {
  protected AtomicLong identityCounter = new AtomicLong(0);

  protected final List<E> entities = new ArrayList<>();

  public Optional<E> findById(Long id) {
    return entities.stream()
        .filter(entity -> entity.getId().equals(id))
        .findFirst();
  }

  public void deleteById(Long id) {
    this.findById(id).ifPresent(entities::remove);
  }

  public E save(E entityToSave) {
    return Optional.ofNullable(entityToSave.getId())
        .flatMap(id -> findById(id)
            .map(entity -> {
              entities.set(entities.indexOf(entity), entityToSave);
              return entityToSave;
            }))
        .orElseGet(() -> {
          entityToSave.setId(identityCounter.incrementAndGet());
          entities.add(entityToSave);
          return entityToSave;
        });
  }

}
