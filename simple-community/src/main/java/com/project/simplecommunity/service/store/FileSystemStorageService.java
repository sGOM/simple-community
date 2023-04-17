package com.project.simplecommunity.service.store;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

//TODO: 추후 AWS S3로 저장소 변경
@Slf4j
@Service
public class FileSystemStorageService implements StorageService {
    private final Path rootLocation = Paths.get("D:\\output");

    @Override
    public void store(List<MultipartFile> multipartFiles) {
        try {
            for (MultipartFile file : multipartFiles) {
                Path destinationFile = this.rootLocation.resolve(
                        Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();

                try (InputStream inputStream = file.getInputStream()) {
                    log.info("# store coffee image!");
                    Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file.", e);
        }
    }

}
