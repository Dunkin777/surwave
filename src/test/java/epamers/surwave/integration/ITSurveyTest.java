package epamers.surwave.integration;

import com.jayway.restassured.RestAssured;
import epamers.surwave.entities.ClassicSurvey;
import epamers.surwave.entities.Option;
import epamers.surwave.entities.Survey;
import epamers.surwave.repos.OptionRepository;
import epamers.surwave.repos.SurveyRepository;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ITSurveyTest extends IntegrationTest {

  @Autowired
  private SurveyRepository surveyRepository;

  @Autowired
  private OptionRepository optionRepository;

  private final String AUTHOR = "Some Author";
  private final String MEDIA_URL = "http://youtube.com/supervideo256";
  private final String TITLE = "Elton John - Komarinskaya (feat. Ella Fitzgerald)";
  private final String COMMENT = "Starts in D#, then sudden change to another religion.";

  @Before
  public void setUp() {

    RestAssured.port = port;
  }

  @Test
  public void surveyController_successCase() {

    Option option = Option.builder()
        .author(AUTHOR)
        .mediaUrl(MEDIA_URL)
        .title(TITLE)
        .comment(COMMENT)
        .build();

    Option createdOption = optionRepository.save(option);

    Survey survey = ClassicSurvey.builder()
        .choicesByUser(2)
        .build();

    survey.setOptions(Set.of(createdOption));
    survey.setDescription("superDescription");

    Long savedSurveyId = surveyRepository.save(survey).getId();

    Survey savedSurvey = surveyRepository.findById(savedSurveyId).orElseThrow();

    ClassicSurvey classicSurvey = (ClassicSurvey) savedSurvey;

    System.out.println(classicSurvey.getChoicesByUser());

  }
}