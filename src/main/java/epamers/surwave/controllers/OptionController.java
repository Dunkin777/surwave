package epamers.surwave.controllers;

import static epamers.surwave.core.Contract.OPTION_URL;
import static epamers.surwave.core.Contract.UPLOAD_URL;

import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import epamers.surwave.services.MediaUploadService;
import epamers.surwave.services.OptionService;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(OPTION_URL)
public class OptionController {

  private final OptionService optionService;
  private final ConversionService converter;
  private final MediaUploadService uploadService;

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

  @PostMapping("/{id}" + UPLOAD_URL)
  @ResponseStatus(HttpStatus.OK)
  public void uploadMediaToOption(@PathVariable Long id, @RequestParam("file") MultipartFile file) {

    Option option = optionService.getById(id);
    uploadService.upload(file, option.getTitle());
  }

  @PutMapping("/{id}")
  public void updateOption(@PathVariable Long id, @RequestBody @Valid OptionForm optionForm) {
    optionService.update(id, converter.convert(optionForm, Option.class));
  }

  @DeleteMapping("/{id}")
  public void deleteOption(@PathVariable Long id) {
    optionService.delete(id);
  }
}
