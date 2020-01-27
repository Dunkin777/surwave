package epamers.surwave.repos.custom;

import java.util.Map;

public interface CustomSurveyRepository {

  Map<Long, String> getSongIDsWithUserIDsBySurveyId(Long surveyId);
}
