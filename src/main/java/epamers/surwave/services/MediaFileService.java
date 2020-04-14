package epamers.surwave.services;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.ObjectMetadata;
import epamers.surwave.core.exceptions.FileStorageException;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaFileService {

  private final S3Service s3Service;

  public String upload(MultipartFile file, Long songId) {
    String songS3Key = songId + ".mp3";

    try {
      String storageKey = s3Service.putObject(songS3Key, file.getInputStream(), new ObjectMetadata());
      log.info("Song ID={} was successfully uploaded.", songId);
      return storageKey;
    } catch (IOException | SdkClientException e) {
      log.error("Failed to load file", e);
      throw new FileStorageException("Could not store file " + file.getOriginalFilename(), e);
    }
  }

  public String getMediaPresignedUrl(String objectKey) {
    return s3Service.getPresignedURL(objectKey);
  }
}
