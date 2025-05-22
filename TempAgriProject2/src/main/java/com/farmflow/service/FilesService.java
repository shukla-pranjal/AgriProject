package com.farmflow.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String uploadFile(MultipartFile file) throws Exception;

    boolean uploadFileBoolean(MultipartFile file) throws Exception;

    Resource downloadFile(String savedName) throws Exception;

    void deleteFile(String savedName) throws Exception;
}
