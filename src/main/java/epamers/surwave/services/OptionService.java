package epamers.surwave.services;

import static java.util.stream.Collectors.toSet;

import epamers.surwave.entities.Option;
import epamers.surwave.repos.OptionRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptionService {

  private final OptionRepository optionRepository;

  public List<Option> getAll() {
    return optionRepository.findAll();
  }

  public Option getById(Long id) {
    return optionRepository.findById(id).orElseThrow();
  }

  @Transactional
  public Option create(Option option) {

    if (option == null) {
      throw new IllegalArgumentException();
    }
    return optionRepository.save(option);
  }

  @Transactional
  public void update(Long id, Option option) {

    if (option == null) {
      throw new IllegalArgumentException();
    }

    if (!optionRepository.existsById(id)) {
      throw new NoSuchElementException();
    }

    option.setId(id);
    optionRepository.save(option);
  }

  @Transactional
  public void delete(Long id) {

    if (!optionRepository.existsById(id)) {
      throw new NoSuchElementException();
    }

    optionRepository.deleteById(id);
  }

  public Set<Option> process(Set<Option> rawOptions) {

    return rawOptions.stream()
        .map(Option::getId)
        .map(this::getById)
        .collect(toSet());
  }
}
