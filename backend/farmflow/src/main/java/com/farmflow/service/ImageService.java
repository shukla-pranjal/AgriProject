package com.farmflow.service;

import com.farmflow.dto.ImageDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    List<ImageDTO> getAllImages();

    List<ImageDTO> getImagesByEntity(EntityType entityType, Integer entityId)throws Exception ;

    List<ImageDTO> getImagesByFileType(FileType fileType);

    List<ImageDTO> searchImages(EntityType entityType, Integer entityId, FileType fileType, Long minSize, Long maxSize, String fileName);

    Page<ImageDTO> getImagesByEntityPaged(EntityType entityType, Integer entityId, PaginationRequest req)throws Exception ;

    Page<ImageDTO> searchImagesPaged(PaginationRequest req, EntityType entityType, Integer entityId, FileType fileType, Long minSize, Long maxSize, String fileName);

    Page<ImageDTO> getAllImagesPaged(PaginationRequest req);

    ImageDTO updateImage(Integer id, MultipartFile file, EntityType entityType, Integer entityId)throws Exception ;

    ImageDTO createImage(MultipartFile file, EntityType entityType, Integer entityId)throws Exception ;

    ImageDTO getImageById(Integer id)throws Exception ;

    void deleteImage(Integer id)throws Exception ;

}
