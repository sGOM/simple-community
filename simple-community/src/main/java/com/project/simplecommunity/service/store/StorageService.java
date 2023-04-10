package com.project.simplecommunity.service.store;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    void store(List<MultipartFile> multipartFileList);
}
