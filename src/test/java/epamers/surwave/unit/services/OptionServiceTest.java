package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

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
  private final Long NON_EXISTING_ID = 36L;
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

    when(optionRepository.findById(ID)).thenReturn(Optional.of(option));
    when(optionRepository.findAll()).thenReturn(List.of(option));
    when(optionRepository.save(option)).thenReturn(option);
  }

  @Test
  public void getAll_success() {

    List<Option> options = optionService.getAll();

    assertEquals(1, options.size());
    assertTrue(options.contains(option));
  }

  @Test
  public void getById_existingId_success() {

    Option returnedOption = optionService.getById(ID);

    assertEquals(option, returnedOption);
  }

  @Test(expected = NoSuchElementException.class)
  public void getById_nonExistingId_exception() {

    optionService.getById(13L);
  }

  @Test
  public void create_validOption_success() {

    Option returnedOption = optionService.create(option);

    verify(optionRepository, times(1)).save(option);
    assertEquals(option, returnedOption);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_nullArgument_exception() {

    optionService.create(null);
  }

  @Test
  public void update_validCase_success() {

    optionService.update(ID, option);

    verify(optionRepository, times(1)).findById(ID);
    verify(optionRepository, times(1)).save(option);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonExistingId_exception() {

    optionService.update(NON_EXISTING_ID, option);

    verify(optionRepository, never()).save(option);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullOption_exception() {

    optionService.update(ID, null);

    verify(optionRepository, never()).save(option);
  }

  @Test
  public void delete_existingId_success() {

    optionService.delete(ID);

    verify(optionRepository, times(1)).deleteById(ID);
  }

  @Test(expected = NoSuchElementException.class)
  public void delete_nonExistingId_exception() {

    optionService.delete(NON_EXISTING_ID);

    verify(optionRepository, never()).deleteById(NON_EXISTING_ID);
  }
}