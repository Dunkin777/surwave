package epamers.surwave.services;

import com.amazonaws.HttpMethod;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class S3Service {

  @Value("${surwave.uploadDir:surwave_media}")
  private String uploadDirectory;

  @Value("${surwave.s3-bucket:surwtest}")
  private String bucketName;

  private AmazonS3 s3client;

  @PostConstruct
  public void init() {
    s3client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.EU_WEST_2)
        .build();
  }

  public String putObject(String key, InputStream input, ObjectMetadata metadata) {
    String s3FileKey = uploadDirectory + "/" + key;
    s3client.putObject(bucketName, s3FileKey, input, metadata);

    return s3FileKey;
  }

  public String getPresignedURL(String objectKey) {
    OffsetDateTime currentTime = OffsetDateTime.now(ZoneOffset.UTC).plusHours(1);
    Date expiration = Date.from(currentTime.toInstant());

    GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, objectKey)
        .withMethod(HttpMethod.GET)
        .withExpiration(expiration);

    URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);

    return url.toString();
  }
}
