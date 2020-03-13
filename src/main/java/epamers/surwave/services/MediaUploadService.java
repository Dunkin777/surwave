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
public class MediaUploadService {

  @Value("${app.upload.dir}")
  private String uploadPath;

  public void upload(MultipartFile file, Long songId) {

    Path copyLocation = Paths.get(getWorkDirectory().toString() + songId + ".mp3");

    try {
      file.transferTo(copyLocation);
    } catch (IOException e) {
      log.error("Failed to load file", e);
      throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!");
    }
  }

  private Path getWorkDirectory() {
    Path path = Paths.get(System.getProperty("user.dir") + File.separator + uploadPath + File.separator);

    if (!Files.exists(path)) {
      try {
        Files.createDirectory(path);
      } catch (IOException e) {
        log.error("Failed to load file", e);
        throw new FileStorageException("Cannot create upload directory at" + path.toString());
      }
    }

    return path;
  }
}
