package epamers.surwave.unit.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import epamers.surwave.controllers.OptionController;
import epamers.surwave.dtos.OptionForm;
import epamers.surwave.dtos.OptionView;
import epamers.surwave.entities.Option;
import epamers.surwave.services.OptionService;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.convert.ConversionService;

public class OptionControllerTest {

  @InjectMocks
  OptionController optionController;

  @Mock
  OptionService optionService;

  @Mock
  ConversionService converter;

  private final Long ID = 156L;
  private final Long NON_EXISTING_ID = 36L;
  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private Option option;
  private OptionView optionView;
  private OptionForm optionForm;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    option = Option.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .id(ID)
        .build();

    optionForm = OptionForm.builder().build();
    optionView = OptionView.builder().build();

    when(optionService.getAll()).thenReturn(List.of(option));
    when(optionService.getById(ID)).thenReturn(option);
    when(converter.convert(option, OptionView.class)).thenReturn(optionView);
    when(converter.convert(optionForm, Option.class)).thenReturn(option);
  }

  @Test
  public void getAllAnswers_success() {

    List<OptionView> returnedOptions = optionController.getAllOptions();

    assertEquals(1, returnedOptions.size());
    assertTrue(returnedOptions.contains(optionView));
  }

  @Test
  public void getOption_existingId_success() {

    OptionView returnedOption = optionController.getOption(ID);

    assertEquals(optionView, returnedOption);
  }
}