package epamers.surwave.services;

import static com.google.common.base.Strings.isNullOrEmpty;
import static epamers.surwave.core.ExceptionMessageContract.SONG_FILE_INVALID_EXTENSION;
import static epamers.surwave.core.ExceptionMessageContract.SONG_UPLOAD_FAILED;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import epamers.surwave.configuration.cache.CacheConstants;
import epamers.surwave.core.exceptions.FileStorageException;
import epamers.surwave.core.exceptions.YouTubeException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileService {

  public static final String SONG_SUCCESSFULLY_UPLOADED = "Song with ID %s was successfully uploaded.";
  public static final Set<String> SUPPORTED_EXTENSIONS = Set.of("wav", "mp3", "mp4", "aiff", "aif", "flac", "wma", "ogg", "opus");

  private final S3Service s3Service;
  private final YoutubeService youtubeService;

  public String upload(MultipartFile file, Long songId) {
    String extension = FilenameUtils.getExtension(file.getOriginalFilename());

    if (isNullOrEmpty(extension) || !SUPPORTED_EXTENSIONS.contains(extension.toLowerCase())) {
      throw new FileStorageException(SONG_FILE_INVALID_EXTENSION);
    }

    String songS3Key = songId + "." + extension.toLowerCase();

    try {
      String storageKey = s3Service.putObject(songS3Key, file.getInputStream(), new ObjectMetadata());
      log.info(String.format(SONG_SUCCESSFULLY_UPLOADED, songId));

      return storageKey;
    } catch (IOException | SdkClientException e) {
      log.error(String.format(SONG_UPLOAD_FAILED, file.getOriginalFilename()), e);
      throw new FileStorageException(String.format(SONG_UPLOAD_FAILED, file.getOriginalFilename()), e);
    }
  }

  @Cacheable(value = CacheConstants.MEDIA_URLS, keyGenerator = CacheConstants.COMMON_KEY_GENERATOR)
  public String getMediaPresignedUrl(String objectKey) {
    return s3Service.getPresignedURL(objectKey);
  }

  public String uploadFromYoutube(String youtubeLink, Long songId) {
    String path = youtubeService.download(youtubeLink, songId);
    try {
      MultipartFile multipartFile = getMultipartFile(path);

      return upload(multipartFile, songId);
    } finally {
      deleteFile(path);
    }
  }

  private void deleteFile(String path) {
    try {
      Files.delete(Paths.get(path));
    } catch (IOException e) {
      log.error("Error during file removing", e);
    }
  }

  private MultipartFile getMultipartFile(String path) {
    MultipartFile multipartFile;

    try {
      File file = new File(path);
      FileInputStream input = new FileInputStream(file);
      multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
    } catch (IOException e) {
      throw new YouTubeException("Converting to multipart file went wrong", e);
    }
    return multipartFile;
  }
}
