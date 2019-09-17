package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import epamers.surwave.entities.Option;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.services.OptionService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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

  private final Long ID = 156L;
  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private Option option;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    option = Option.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .id(ID)
        .build();
  }

  @Test
  public void getAll_success() {

    when(optionRepository.findAll()).thenReturn(List.of(option));

    List<Option> options = optionService.getAll();

    assertEquals(1, options.size());
    assertTrue(options.contains(option));
  }

  @Test
  public void getById_existingId_success() {

    when(optionRepository.findById(ID)).thenReturn(Optional.of(option));

    Option returnedOption = optionService.getById(ID);

    assertEquals(option, returnedOption);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonExistingId_exception() {

    optionService.getById(13L);
  }
}