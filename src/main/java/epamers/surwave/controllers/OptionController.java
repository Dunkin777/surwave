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

  @GetMapping("/hi")
  String getAnswer() {
    optionRepository.findAll();
    return "Well, hello";
  }
}
