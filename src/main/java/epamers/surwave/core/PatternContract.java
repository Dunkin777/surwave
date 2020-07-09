package epamers.surwave.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternContract {

  public static final String YOUTUBE_LINK_PATTERN = "http(?:s?):\\/\\/(?:www\\.)?youtu(?:be\\.com\\/watch\\?v=|\\.be\\/)([\\w\\-\\_]*)(&(amp;)?[\\w\\?=]*)?";
  public static final String MP3_EXTENSION = ".mp3";
}
