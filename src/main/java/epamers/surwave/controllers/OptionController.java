package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.OPTION_URL;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import epamers.surwave.services.OptionService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(OPTION_URL)
public class OptionController {

  private final OptionService optionService;
  private final ConversionService converter;

  @GetMapping("/all")
  public List<OptionView> getAllOptions() {

    return optionService.getAll().stream()
        .map(o -> converter.convert(o, OptionView.class))
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public OptionView getOption(@PathVariable Long id) {

    Option option = optionService.getById(id);
    return converter.convert(option, OptionView.class);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createOption(@RequestBody @Valid OptionForm optionForm, HttpServletResponse response) {

    Option option = optionService.create(converter.convert(optionForm, Option.class));
    response.addHeader("Location", OPTION_URL + "/" + option.getId());
  }

  @PutMapping("/{id}")
  public void updateOption(@PathVariable Long id, @RequestBody @Valid OptionForm optionForm) {

    optionService.update(id, converter.convert(optionForm, Option.class));
  }

  //TODO: remove this endpoint when Survey controller will be ready
  @DeleteMapping("/{id}")
  public void deleteOption(@PathVariable Long id) {

    optionService.delete(id);
  }
}
