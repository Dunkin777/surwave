package epamers.surwave.services;

import epamers.surwave.entities.Option;
import epamers.surwave.repos.OptionRepository;
import java.util.List;
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

    optionRepository.findById(id).orElseThrow();
    option.setId(id);
    optionRepository.save(option);
  }

  @Transactional
  public void delete(Long id) {

    optionRepository.findById(id).orElseThrow();
    optionRepository.deleteById(id);
  }
}
