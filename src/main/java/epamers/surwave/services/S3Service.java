package epamers.surwave.services;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
public class S3Service {

  @Value("${surwave.uploadDir}")
  private String uploadDirectory;

  @Value("${surwave.s3-bucket}")
  private String bucketName;

  private AmazonS3 s3client;

  @PostConstruct
  public void init() {
    s3client = AmazonS3ClientBuilder.standard()
        .withRegion(Regions.EU_WEST_2)
        .build();
  }

  public void putObject(String key, InputStream input, ObjectMetadata metadata) {
    String s3FileKey = uploadDirectory + "/" + key;
    s3client.putObject(bucketName, s3FileKey, input, metadata);
  }
}
