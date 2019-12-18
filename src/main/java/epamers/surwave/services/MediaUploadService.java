package epamers.surwave.services;


import epamers.surwave.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;


@Service
public class MediaUploadService {

    @Value("${app.upload.dir}")
    private String uploadPath;


    public void upload(MultipartFile file){

        try {

            Path copyLocation = Paths
                    .get(getWorkDirectory() + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (Exception e) {

            e.printStackTrace();
            throw new FileStorageException("Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }
    }

    public  String getWorkDirectory() {
        return System.getProperty("user.dir") + File.separator + uploadPath + File.separator;
    }
}
