package epamers.surwave.controllers;

import epamers.surwave.services.MediaUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import static epamers.surwave.core.Contract.UPLOAD_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(UPLOAD_URL)
public class UploadController {

    private final MediaUploadService uploadService;

    @PostMapping(consumes = {"multipart/form-data"})
    public void write(@RequestParam("file") MultipartFile file){

//        uploadService.upload(file, option.getTitle());
    }
}
