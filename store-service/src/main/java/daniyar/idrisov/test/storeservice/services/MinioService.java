package daniyar.idrisov.test.storeservice.services;

import daniyar.idrisov.test.storeservice.configs.properties.MinioProperties;
import daniyar.idrisov.test.storeservice.exceptions.MinioGetFileException;
import daniyar.idrisov.test.storeservice.exceptions.MinioUploadFileException;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient client;
    private final MinioProperties properties;

    public String uploadFileAndGetFilename(MultipartFile file) {
        String filename = createFilename(file.getOriginalFilename());
        try {
            client.putObject(PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(filename)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .build());
        } catch (Exception e) {
            throw new MinioUploadFileException();
        }
        return filename;
    }

    public static String createFilename(String originalFilename) {
        String extension = FilenameUtils.getExtension(originalFilename);
        if (extension == null || extension.isBlank()) {
            originalFilename = originalFilename + "(" + Instant.now().getEpochSecond() + ")";
        } else {
            originalFilename = FilenameUtils.removeExtension(originalFilename) + "(" + Instant.now().getEpochSecond() + ")." + extension;
        }
        return originalFilename;
    }

    public InputStream getObject(String filename) {
        InputStream stream;
        try {
            stream = client.getObject(GetObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(filename)
                    .build());
        } catch (Exception e) {
            throw new MinioGetFileException();
        }
        return stream;
    }
}
