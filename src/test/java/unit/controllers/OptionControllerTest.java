package unit.controllers;

import epamers.surwave.controllers.OptionController;
import epamers.surwave.services.OptionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OptionControllerTest {

  @InjectMocks
  OptionController optionController;

  @Mock
  OptionService optionService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAllAnswers() {
  }
}