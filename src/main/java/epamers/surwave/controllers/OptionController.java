package epamers.surwave.controllers;

import epamers.surwave.converters.FormToOptionConverter;
import epamers.surwave.converters.OptionToViewConverter;
import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import epamers.surwave.services.OptionService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/option")
public class OptionController {

  private final OptionService optionService;
  private final OptionToViewConverter optionToViewConverter;
  private final FormToOptionConverter formToOptionConverter;

  @GetMapping("/all")
  public List<OptionView> getAllAnswers() {

    return optionService.getAll().stream()
        .map(optionToViewConverter::convert)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public OptionView getAnswer(@PathVariable Long id) {

    Option option = optionService.getById(id);
    return optionToViewConverter.convert(option);
  }

  @PostMapping
  public void createAnswer(@RequestBody OptionForm optionForm) {

    optionService.save(formToOptionConverter.convert(optionForm));
  }

  @PutMapping("/{id}")
  public void updateAnswer(@PathVariable Long id, @RequestBody OptionForm optionForm) {

    optionService.update(id, formToOptionConverter.convert(optionForm));
  }
}
