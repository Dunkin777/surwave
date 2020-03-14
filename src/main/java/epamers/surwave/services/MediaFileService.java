package epamers.surwave.services;

import epamers.surwave.core.exceptions.FileStorageException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MediaFileService {

  @Value("${surwave.uploadDir}")
  private String uploadDirectory;

  public String upload(MultipartFile file, Long songId) {

    Path copyLocation = getUploadDirectory().resolve(songId + ".mp3");

    try {
      file.transferTo(copyLocation);
    } catch (IOException e) {
      log.error("Failed to load file", e);
      throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!");
    }

    return copyLocation.toString();
  }

  private Path getUploadDirectory() {
    Path uploadPath = Paths.get(System.getProperty("user.dir") + File.separator + uploadDirectory);

    if (!Files.exists(uploadPath)) {
      try {
        Files.createDirectory(uploadPath);
      } catch (IOException e) {
        log.error("Failed to load file", e);
        throw new FileStorageException("Cannot create upload directory at" + uploadPath.toString());
      }
    }

    return uploadPath;
  }
}
