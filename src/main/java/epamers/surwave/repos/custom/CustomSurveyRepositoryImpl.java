package epamers.surwave.repos.custom;

import java.math.BigInteger;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomSurveyRepositoryImpl implements CustomSurveyRepository {

  private final EntityManager entityManager;

  @Override
  public Map<Long, String> getSongIDsWithUserIDsBySurveyId(Long surveyId) {
    Stream<Tuple> resultStream = entityManager.createNativeQuery(String.format("select user_song.song_id, user_song.user_id"
        + " from user_song"
        + "    join survey_song on user_song.song_id = survey_song.song_id"
        + "    join song on user_song.song_id = song.id"
        + "    join app_user on user_song.user_id = app_user.id"
        + " where survey_song.survey_id = %d", surveyId), Tuple.class).getResultStream();

    return resultStream.collect(Collectors.toMap(
        (Tuple tuple) -> ((BigInteger) tuple.get("song_id")).longValue(),
        (Tuple tuple) -> (String) tuple.get("user_id")
    ));
  }
}
