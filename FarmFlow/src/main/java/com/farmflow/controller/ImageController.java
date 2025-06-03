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
@Slf4j
@RestController
public class ImageController implements ImageEndpoint {

    private final ImageService imageService;
    private final FilesService filesService;
    private static final String CLASS = ImageController.class.getSimpleName();

    @Override
    public ResponseEntity<?> getAll() {
        String m = "getAll";
        log.debug("{}::{} started", CLASS, m);
        List<ImageDTO> list = imageService.getAllImages();
        log.info("{}::{} returned {} images", CLASS, m, list.size());
        return CommonUtil.createBuildResponse(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) throws Exception {
        String m = "getById";
        log.debug("{}::{} id={}", CLASS, m, id);
        try {
            ImageDTO dto = imageService.getImageById(id);
            log.info("{}::{} found image id={}", CLASS, m, id);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} error id={}, {}", CLASS, m, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> create(MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        String m = "create";
        log.debug("{}::{} start upload for {}#{}", CLASS, m, entityType, entityId);
        try {
            ImageDTO dto = imageService.createImage(file, entityType, entityId);
            log.info("{}::{} created id={}", CLASS, m, dto.getId());
            return CommonUtil.createBuildResponse(dto, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("{}::{} failed upload, {}", CLASS, m, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> update(Integer id, MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        String m = "update";
        log.debug("{}::{} id={} for {}#{}", CLASS, m, id, entityType, entityId);
        try {
            ImageDTO dto = imageService.updateImage(id, file, entityType, entityId);
            log.info("{}::{} updated id={}", CLASS, m, id);
            return CommonUtil.createBuildResponse(dto, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} failed id={}, {}", CLASS, m, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> delete(Integer id) throws Exception {
        String m = "delete";
        log.debug("{}::{} id={}", CLASS, m, id);
        try {
            imageService.deleteImage(id);
            log.info("{}::{} deleted id={}", CLASS, m, id);
            return CommonUtil.createBuildResponseMessage("Image deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} failed delete id={}, {}", CLASS, m, id, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByEntity(EntityType entityType, Integer entityId) throws Exception {
        String m = "getByEntity";
        log.debug("{}::{} for {}#{}", CLASS, m, entityType, entityId);
        try {
            List<ImageDTO> list = imageService.getImagesByEntity(entityType, entityId);
            log.info("{}::{} found {} images", CLASS, m, list.size());
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} error {}, {}", CLASS, m, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> getByFileType(FileType fileType) throws Exception {
        String m = "getByFileType";
        log.debug("{}::{} fileType={}", CLASS, m, fileType);
        try {
            List<ImageDTO> list = imageService.getImagesByFileType(fileType);
            log.info("{}::{} found {} images", CLASS, m, list.size());
            return CommonUtil.createBuildResponse(list, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} error {}, {}", CLASS, m, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchImages(EntityType entityType, Integer entityId,
                                          FileType fileType, Long minSize, Long maxSize,
                                          String fileName) {
        String m = "searchImages";
        log.debug("{}::{} params [entity={},id={},type={},minSize={},maxSize={},name={}]",
                CLASS, m, entityType, entityId, fileType, minSize, maxSize, fileName);
        List<ImageDTO> results = imageService.searchImages(
                entityType, entityId, fileType, minSize, maxSize, fileName
        );
        log.info("{}::{} found {} images", CLASS, m, results.size());
        return CommonUtil.createBuildResponse(results, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getAllPaged(PaginationRequest req) {
        String m = "getAllPaged";
        log.debug("{}::{} req={}", CLASS, m, req);
        Page<ImageDTO> page = imageService.getAllImagesPaged(req);
        PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
        log.info("{}::{} page={},size={}", CLASS, m, page.getNumber(), page.getSize());
        return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getByEntityPaged(EntityType entityType, Integer entityId,
                                              PaginationRequest req) throws Exception {
        String m = "getByEntityPaged";
        log.debug("{}::{} entity={},id={}, req={}", CLASS, m, entityType, entityId, req);
        try {
            Page<ImageDTO> page = imageService.getImagesByEntityPaged(entityType, entityId, req);
            PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
            log.info("{}::{} page={},size={}", CLASS, m, page.getNumber(), page.getSize());
            return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} error {}, {}", CLASS, m, e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<?> searchImagesPaged(PaginationRequest req,
                                               EntityType entityType, Integer entityId,
                                               FileType fileType, Long minSize, Long maxSize,
                                               String fileName) {
        String m = "searchImagesPaged";
        log.debug("{}::{} req={}, [entity={},id={},type={},minSize={},maxSize={},name={}]",
                CLASS, m, req, entityType, entityId, fileType, minSize, maxSize, fileName);
        Page<ImageDTO> page = imageService.searchImagesPaged(
                req, entityType, entityId, fileType, minSize, maxSize, fileName
        );
        PaginatedResponse<ImageDTO> resp = PaginatedResponse.fromPage(page);
        log.info("{}::{} page={},size={}", CLASS, m, page.getNumber(), page.getSize());
        return CommonUtil.createBuildResponse(resp, HttpStatus.OK);
    }
    @Override
    public ResponseEntity<byte[]> downloadImage(Integer id) throws Exception {
        String m = "downloadImage";
        log.debug("{}::{} id={}", CLASS, m, id);
        try {
            // Fetch the image metadata to get file details
            ImageDTO imageDTO = imageService.getImageById(id);
            if (imageDTO == null) {
                log.warn("{}::{} image not found for id={}", CLASS, m, id);
                throw new Exception("Image with ID " + id + " not found");
            }

            // Fetch the raw image bytes using FilesService
            String savedName = imageDTO.getSavedName();
            if (savedName == null || savedName.isEmpty()) {
                log.warn("{}::{} savedName is empty for id={}", CLASS, m, id);
                throw new Exception("Saved name not available for image ID " + id);
            }

            Resource resource = filesService.downloadFile(savedName);
            if (resource == null || !resource.exists() || !resource.isReadable()) {
                log.warn("{}::{} image resource not available for id={}, savedName={}", CLASS, m, id, savedName);
                throw new Exception("Image data not available for ID " + id);
            }

            // Convert Resource to byte array
            byte[] imageBytes = resource.getInputStream().readAllBytes();
            if (imageBytes.length == 0) {
                log.warn("{}::{} image bytes empty for id={}, savedName={}", CLASS, m, id, savedName);
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
                    log.warn("{}::{} unsupported file type {} for id={}, defaulting to octet-stream",
                            CLASS, m, imageDTO.getFileType(), id);
                    break;
            }

            // Set additional headers for better client handling
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setContentLength(imageDTO.getFileSize() != null ? imageDTO.getFileSize() : imageBytes.length);

            log.info("{}::{} downloaded image id={}, fileName={}, size={}",
                    CLASS, m, id, fileName, imageDTO.getFileSize());
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("{}::{} failed to download id={}, error: {}", CLASS, m, id, e.getMessage());
            throw e;
        }
    }
}


