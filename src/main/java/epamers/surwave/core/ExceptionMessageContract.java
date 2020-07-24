package epamers.surwave.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionMessageContract {

  public static final String OBJECT_IS_NULL = "Received NULL object";

  public static final String USER_CANNOT_CHANGE_PASSWORD = "Cannot change password of OAuth user";
  public static final String USER_NOT_FOUND_BY_NAME = "User with name '%s' was not found in database";
  public static final String USER_NOT_FOUND_BY_ID = "User with id %s was not found in database";
  public static final String USER_INVALID_NUMBER_OF_VOTES = "Found inconsistent number of votes for current user";
  public static final String USER_NOT_AUTHENTICATED = "Anonymous requests are not supported. Please, use /login";

  public static final String VOTING_FOR_ZERO_OPTIONS = "Cannot vote for zero options";
  public static final String VOTING_NOT_ONE_SURVEY = "Got incorrect number of surveys involved in voting";
  public static final String VOTING_ALREADY_VOTED = "User already voted in this survey";
  public static final String VOTING_MORE_THAN_ONE_VOTE_FOR_OPTION = "Cannot give more than one vote to the same option";
  public static final String VOTING_CHOICES_BY_USER_NOT_SATISFIED = "Invalid number of votes! expected %d but received %d";
  public static final String VOTING_FOR_YOUR_OPTION = "It is prohibited to vote for option that you've proposed";
  public static final String VOTING_INVALID_RATING_FOR_CLASSIC_TYPE = "'1' is the only valid value for vote rating in classic surveys";
  public static final String VOTING_WRONG_SURVEY_STATE = "Voting is impossible in that Survey state";

  public static final String RESULTS_INVALID_SURVEY_STATE = "Results are available only for surveys in 'STOPPED' state";

  public static final String REQUEST_SIZE_IS_TOO_BIG = "Maximum allowed request size is 21MB";

  public static final String SONG_FILE_IS_TOO_BIG = "Cannot upload, file is too big. Maximum allowed size is 20MB";
  public static final String SONG_FILE_INVALID_EXTENSION = "Given file extension is not supported";
  public static final String SONG_UPLOAD_FAILED = "Failed to store file '%s'";
  public static final String SONG_IS_NULL_CREATION = "Got NULL song, cannot create";
  public static final String SONG_NOT_FOUND = "Song with id %d was not found in database";

  public static final String SURVEY_UNKNOWN_TYPE = "Survey type '%s' is not supported";
  public static final String SURVEY_NOT_FOUND = "Survey with id %d was not found in database";
  public static final String SURVEY_IS_NULL_CREATION = "Got NULL survey, cannot create";
  public static final String SURVEY_IS_NULL_MODIFICATION = "Got NULL survey, cannot update";
  public static final String SURVEY_RESTRICTED_MODIFICATION = "Modification of survey type, proposals per user or votes per user is restricted for existing survey";

  public static final String OPTION_NOT_FOUND = "Option with id %d was not found in database";
  public static final String OPTION_IS_NULL_CREATION = "Cannot add NULL option to a survey";
  public static final String OPTION_ALREADY_EXISTS = "Given survey already contains this song";

  public static final String YOUTUBE_DL_ERROR = "Error during downloading from YouTube";
  public static final String NO_SOURCE = "There's no data source in request: nor MediaFile, nor source link";
  public static final String TOO_MANY_SOURCES = "There are too many sources, cannot resolve what to use";
  public static final String INCORRECT_YOUTUBE_LINK_FORMAT = "Could not recognize the youtubelink %s";

  public static final String UNEXPECTED_EXCEPTION = "Unexpected internal error occurred on the server: %s";
}
