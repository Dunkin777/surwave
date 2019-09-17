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

    return optionRepository.getOne(id);
  }

  @Transactional
  public Long save(Option option) {

    return optionRepository.save(option).getId();
  }

  public void update(Long id, Option option) {

    if (option == null) {
      throw new IllegalArgumentException();
    }

    option.setId(id);
    optionRepository.save(option);
  }

  public void delete(Long id) {

    optionRepository.deleteById(id);
  }
}
