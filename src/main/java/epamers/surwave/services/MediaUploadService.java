package epamers.surwave.services;

import epamers.surwave.core.exceptions.FileStorageException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class MediaUploadService {

  @Value("${app.upload.dir}")
  private String uploadPath;

  public void upload(MultipartFile file) {
    String fileName = Optional.ofNullable(file.getOriginalFilename()).orElse("defaultFileName.mp3");

    Path copyLocation = Paths.get(getWorkDirectory() + File.separator + StringUtils.cleanPath(fileName));

    try {
      file.transferTo(copyLocation);
    } catch (IOException e) {
      log.error("Failed to load file", e);
      throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!");
    }
  }

  private String getWorkDirectory() {
    return System.getProperty("user.dir") + File.separator + uploadPath + File.separator;
  }
}
