package epamers.surwave.unit.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
  private final Long NONEXISTENT_ID = 36L;
  private final String AUTHOR = "Some Author";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";
  private Option option;

  @Before
  public void setUp() {

    MockitoAnnotations.initMocks(this);
    option = Option.builder()
        .author(AUTHOR)
        .title(TITLE)
        .id(ID)
        .comment(COMMENT)
        .build();

    when(optionRepository.existsById(ID)).thenReturn(true);
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

    when(optionRepository.existsById(ID)).thenReturn(false);

    optionService.getById(13L);
  }

  @Test
  public void create_validOption_success() {

    Option returnedOption = optionService.create(option);

    verify(optionRepository).save(option);
    assertEquals(option, returnedOption);
  }

  @Test(expected = IllegalArgumentException.class)
  public void create_nullArgument_exception() {

    optionService.create(null);
  }

  @Test
  public void update_validCase_success() {

    optionService.update(ID, option);

    verify(optionRepository).save(option);
  }

  @Test(expected = NoSuchElementException.class)
  public void update_nonExistingId_exception() {

    optionService.update(NONEXISTENT_ID, option);

    verify(optionRepository, never()).save(option);
  }

  @Test(expected = IllegalArgumentException.class)
  public void update_nullOption_exception() {

    optionService.update(ID, null);

    verify(optionRepository, never()).save(option);
  }
}