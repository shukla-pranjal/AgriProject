package com.farmflow.service.impl;

import com.farmflow.exception.StorageException;
import com.farmflow.service.FilesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class DiskFilesService implements FilesService {

    private final Path rootDir;

    // Inject with @Value("${file.upload.path}") or @ConfigurationProperties
    public DiskFilesService(@Value("${file.upload.path}") String uploadPath) {
        this.rootDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new StorageException("Could not initialize upload directory", e);
        }
    }

    @Override
    public String uploadFile(MultipartFile file) throws StorageException {
        String storedName = buildStoredFileName(file.getOriginalFilename());
        Path target = rootDir.resolve(storedName);
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Saved file {} → {}", file.getOriginalFilename(), storedName);
            return storedName;
        } catch (IOException e) {
            log.error("Failed to store file {}", storedName, e);
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public boolean uploadFileBoolean(MultipartFile file) throws StorageException {
        try {
            uploadFile(file);
            return true;
        } catch (StorageException e) {
            return false;
        }
    }

    @Override
    public Resource downloadFile(String savedName) throws StorageException {
        try {
            Path file = rootDir.resolve(savedName).normalize();
            Resource res = new UrlResource(file.toUri());
            if (!res.exists() || !res.isReadable()) {
                throw new StorageException("File not found or unreadable: " + savedName);
            }
            return res;
        } catch (MalformedURLException e) {
            throw new StorageException("Failed to load file: " + savedName, e);
        }
    }

    @Override
    public void deleteFile(String savedName) throws StorageException {
        try {
            Files.deleteIfExists(rootDir.resolve(savedName));
            log.info("Deleted file {}", savedName);
        } catch (IOException e) {
            throw new StorageException("Failed to delete file: " + savedName, e);
        }
    }


    private String buildStoredFileName(String original) {
        String base = removeExtension(original);
        String ext  = getExtension(original);
        String uuid = UUID.randomUUID().toString();
        return base + "_" + uuid + (ext.isEmpty() ? "" : "." + ext);
    }

    // Helpers:
    private static String removeExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') < 0) return fileName;
        return fileName.substring(0, fileName.lastIndexOf('.'));
    }
    private static String getExtension(String fileName) {
        if (fileName == null || fileName.lastIndexOf('.') < 0) return "";
        return fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
    }
}