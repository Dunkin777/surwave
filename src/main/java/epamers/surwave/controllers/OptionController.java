package epamers.surwave.controllers;

import epamers.surwave.repos.OptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/option")
public class OptionController {

  private final OptionRepository optionRepository;

  @GetMapping("/all")
  String getAllAnswers() {
    optionRepository.findAll();
    return "Well, hello";
  }
}
