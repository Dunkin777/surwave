package epamers.surwave.unit.services;

import epamers.surwave.repos.OptionRepository;
import epamers.surwave.services.OptionService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class OptionServiceTest {

  @InjectMocks
  OptionService optionService;

  @Mock
  OptionRepository optionRepository;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void getAllAnswers() {

  }
}