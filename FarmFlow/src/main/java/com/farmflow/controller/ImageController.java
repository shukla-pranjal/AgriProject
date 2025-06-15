package com.farmflow.controller;


import com.farmflow.dto.ImageDTO;
import com.farmflow.dto.PaginatedResponse;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.endpoint.ImageEndpoint;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import com.farmflow.service.FilesService;
import com.farmflow.service.ImageService;
import com.farmflow.util.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ImageController implements ImageEndpoint {

    private final ImageService imageService;
    private final FilesService filesService;

    @Override
    public ResponseEntity<?> getAll() {
        List<ImageDTO> list = imageService.getAllImages();
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        ImageDTO dto = imageService.getImageById(id);
        return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> create(MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        ImageDTO dto = imageService.createImage(file, entityType, entityId);
        return CommonUtil.createBuildResponse(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> update(Integer id, MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        ImageDTO dto = imageService.updateImage(id, file, entityType, entityId);
        return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        imageService.deleteImage(id);
        return CommonUtil.createBuildResponseMessage("Image deleted successfully", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByEntity(EntityType entityType, Integer entityId) throws Exception {
        List<ImageDTO> list = imageService.getImagesByEntity(entityType, entityId);
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByFileType(FileType fileType) {
        List<ImageDTO> list = imageService.getImagesByFileType(fileType);
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchImages(EntityType entityType, Integer entityId,
                                          FileType fileType, Long minSize, Long maxSize,
                                          String fileName) {
        List<ImageDTO> results = imageService.searchImages(
                entityType, entityId, fileType, minSize, maxSize, fileName
        );
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest req) {
        Page<ImageDTO> page = imageService.getAllImagesPaged(req);
        PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByEntityPaged(EntityType entityType, Integer entityId,
                                              PaginationRequest req) throws Exception {
        Page<ImageDTO> page = imageService.getImagesByEntityPaged(entityType, entityId, req);
        PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> searchImagesPaged(PaginationRequest req,
                                               EntityType entityType, Integer entityId,
                                               FileType fileType, Long minSize, Long maxSize,
                                               String fileName) {
        Page<ImageDTO> page = imageService.searchImagesPaged(
                req, entityType, entityId, fileType, minSize, maxSize, fileName
        );
        PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
        return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> downloadImage(Integer id) throws Exception {  // TODO better this method
        // Fetch the image metadata to get file details
        ImageDTO imageDTO = imageService.getImageById(id);
        if (imageDTO == null) {
            throw new Exception("Image with ID " + id + " not found");
        }

        // Fetch the raw image bytes using FilesService
        String savedName = imageDTO.getSavedName();
        if (savedName == null || savedName.isEmpty()) {
            throw new Exception("Saved name not available for image ID " + id);
        }

        Resource resource = filesService.downloadFile(savedName);
        if (resource == null || !resource.exists() || !resource.isReadable()) {
            throw new Exception("Image data not available for ID " + id);
        }

        // Convert Resource to byte array
        byte[] imageBytes = resource.getInputStream().readAllBytes();
        if (imageBytes.length == 0) {
            throw new Exception("Image data is empty for ID " + id);
        }

        // Use the savedName from ImageDTO as the file name
        String fileName = savedName;

        HttpHeaders headers = new HttpHeaders();
        // Set Content-Type based on the fileType from ImageDTO
        switch (imageDTO.getFileType()) {
            case IMAGE_PNG:
                headers.setContentType(MediaType.IMAGE_PNG);
                break;
            case IMAGE_JPEG:
                headers.setContentType(MediaType.IMAGE_JPEG);
                break;
            default:
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                break;
        }

        // Set additional headers for better client handling
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(imageDTO.getFileSize() != null ? imageDTO.getFileSize() : imageBytes.length);

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}

