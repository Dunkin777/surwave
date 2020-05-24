package epamers.surwave.services;

import static epamers.surwave.core.ExceptionMessageContract.SONG_UPLOAD_FAILED;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import epamers.surwave.configuration.cache.CacheConstants;
import epamers.surwave.core.exceptions.FileStorageException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileService {

  public static final String SONG_SUCCESSFULLY_UPLOADED = "Song with ID %s was successfully uploaded.";
  public static final String FAILED_TO_LOAD_FILE = "Failed to load file";

  private final S3Service s3Service;

  public String upload(MultipartFile file, Long songId) {
    String songS3Key = songId + ".mp3";

    try {
      String storageKey = s3Service.putObject(songS3Key, file.getInputStream(), new ObjectMetadata());
      log.info(String.format(SONG_SUCCESSFULLY_UPLOADED, songId));

      return storageKey;
    } catch (IOException | SdkClientException e) {
      log.error(FAILED_TO_LOAD_FILE, e);
      throw new FileStorageException(String.format(SONG_UPLOAD_FAILED, file.getOriginalFilename()), e);
    }
  }

  @Cacheable(value = CacheConstants.MEDIA_URLS, keyGenerator = CacheConstants.COMMON_KEY_GENERATOR)
  public String getMediaPresignedUrl(String objectKey) {
    return s3Service.getPresignedURL(objectKey);
  }
}
