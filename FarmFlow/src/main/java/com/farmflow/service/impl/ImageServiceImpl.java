package com.farmflow.service.impl;

import com.farmflow.dto.ImageDTO;
import com.farmflow.dto.PaginationRequest;
import com.farmflow.entity.Farmer;
import com.farmflow.entity.Image;
import com.farmflow.entity.Product;
import com.farmflow.entity.User;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import com.farmflow.exception.ResourceNotFoundException;
import com.farmflow.repository.FarmerRepository;
import com.farmflow.repository.ImageRepository;
import com.farmflow.repository.ProductRepository;
import com.farmflow.repository.UserRepository;
import com.farmflow.service.AuthService;
import com.farmflow.service.FilesService;
import com.farmflow.service.ImageService;
import com.farmflow.util.Constants;
import com.farmflow.util.Validation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.farmflow.util.Constants.MAX_IMAGES_FARMER;
import static com.farmflow.util.Constants.MAX_IMAGES_PRODUCT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final FilesService filesService;
    private final Validation validation;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final FarmerRepository farmerRepository;
    private final AuthService authService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "imageCache", key = "'all'"),
            @CacheEvict(value = "imageCache", key = "#entityType + '-' + #entityId"),
            @CacheEvict(value = "imageCache", key = "#file.contentType != null ? T(com.yourpackage.FileType).fromMimeType(#file.contentType).name() : null")
    })
    public ImageDTO createImage(MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        log.debug("createImage: validating file and entity");
        validation.imageValidate(file, entityType, entityId);

        validateEntityExists(entityType, entityId);

        log.debug("createImage: storing file");
        String savedName = filesService.uploadFile(file);

        Image img = new Image();
        img.setFileName(file.getOriginalFilename());
        img.setSavedName(savedName);
        img.setFileType(FileType.fromMimeType(file.getContentType()));
        img.setFileSize(file.getSize());

        switch (entityType) {
            case USER:
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, entityId)));

                if (!(authService.isOwnerOrAdmin(user.getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);

                if (user.getImage() != null) {
                    Image oldImage = user.getImage();
                    filesService.deleteFile(oldImage.getSavedName());
                    oldImage.setFileName(file.getOriginalFilename());
                    oldImage.setSavedName(savedName);
                    oldImage.setFileType(img.getFileType());
                    oldImage.setFileSize(img.getFileSize());
                    img = imageRepository.save(oldImage);
                } else {
                    img = imageRepository.save(img);
                    user.setImage(img);
                    userRepository.save(user);
                }
                break;

            case PRODUCT:
                Product product = productRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, entityId)));
                if (!(authService.isOwnerOrAdmin(product.getFarmer().getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                if (product.getImages().size() >= Constants.MAX_IMAGES_PRODUCT) {
                    throw new Exception(String.format(Constants.MAX_IMAGES_REACHED, Constants.PRODUCT, entityId, Constants.MAX_IMAGES_PRODUCT));
                }
                img = imageRepository.save(img);
                product.getImages().add(img);
                if (product.getImages().size() == 1) {
                    img.setIsPrimary(true);
                    imageRepository.save(img);
                }
                productRepository.save(product);
                break;

            case FARMER:
                Farmer farmer = farmerRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, entityId)));

                if (!(authService.isOwnerOrAdmin(farmer.getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                if (farmer.getImages().size() >= Constants.MAX_IMAGES_FARMER) {
                    throw new Exception(String.format(Constants.MAX_IMAGES_REACHED, Constants.FARMER, entityId, Constants.MAX_IMAGES_FARMER));
                }
                img = imageRepository.save(img);
                farmer.getImages().add(img);
                if (farmer.getImages().size() == 1) {
                    img.setIsPrimary(true);
                    imageRepository.save(img);
                }
                farmerRepository.save(farmer);
                break;

            default:
                throw new IllegalArgumentException(String.format(Constants.UNSUPPORTED_ENTITY_TYPE, entityType));
        }

        log.info("createImage: saved Image[id={}]", img.getId());
        return convertToDTO(img);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "imageCache", key = "'all'"),
            @CacheEvict(value = "imageCache", key = "#entityType + '-' + #entityId"),
            @CacheEvict(value = "imageCache", key = "#existing.fileType.name()")
    },
            put = {
                    @CachePut(value = "imageCache", key = "#result.id")
            })

    public ImageDTO updateImage(Integer id, MultipartFile file, EntityType entityType, Integer entityId) throws Exception {
        log.debug("updateImage: fetching existing image[id={}]", id);
        Image existing = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.IMAGE, id)));

        validateEntityExists(entityType, entityId);

        validation.imageValidate(file, entityType, entityId);

        if (file != null && !file.isEmpty()) {
            log.debug("updateImage: replacing stored file");
            filesService.deleteFile(existing.getSavedName());
            String newSaved = filesService.uploadFile(file);
            existing.setSavedName(newSaved);
            existing.setFileName(file.getOriginalFilename());
            existing.setFileType(FileType.fromMimeType(file.getContentType()));
            existing.setFileSize(file.getSize());
        }

        switch (entityType) {
            case USER:
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, entityId)));
                if (!(authService.isOwnerOrAdmin(user.getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                if (user.getImage() == null || !user.getImage().getId().equals(id)) {
                    throw new Exception(String.format(Constants.IMAGE_NOT_BELONG_TO_ENTITY, id, Constants.USER, entityId));
                }
                existing = imageRepository.save(existing);
                break;

            case PRODUCT:
                Product product = productRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, entityId)));
                if (!(authService.isOwnerOrAdmin(product.getFarmer().getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                if (!product.getImages().stream().anyMatch(img -> img.getId().equals(id))) {
                    throw new Exception(String.format(Constants.IMAGE_NOT_BELONG_TO_ENTITY, id, Constants.PRODUCT, entityId));
                }
                existing = imageRepository.save(existing);
                break;

            case FARMER:
                Farmer farmer = farmerRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, entityId)));
                if (!(authService.isOwnerOrAdmin(farmer.getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                if (!farmer.getImages().stream().anyMatch(img -> img.getId().equals(id))) {
                    throw new Exception(String.format(Constants.IMAGE_NOT_BELONG_TO_ENTITY, id, Constants.FARMER, entityId));
                }
                existing = imageRepository.save(existing);
                break;

            default:
                throw new IllegalArgumentException(String.format(Constants.UNSUPPORTED_ENTITY_TYPE, entityType));
        }

        log.info("updateImage: updated Image[id={}]", id);
        return convertToDTO(existing);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "imageCache", key = "#id"),
            @CacheEvict(value = "imageCache", key = "'all'"),
            @CacheEvict(value = "imageCache", key = "#img.entityType + '-' + #img.entityId"),
            @CacheEvict(value = "imageCache", key = "#img.fileType.name()")
    })
    public void deleteImage(Integer id) throws Exception {
        log.debug("deleteImage: fetching image[id={}]", id);
        Image img = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.IMAGE, id)));

        if (img.getEntityType() == null && img.getEntityId() == null) {
            Optional<User> userOptional = userRepository.findByImageId(id);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                if (!(authService.isOwnerOrAdmin(user.getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                user.setImage(null);
                userRepository.save(user);
            }
        }

        log.debug("deleteImage: deleting file and record");
        filesService.deleteFile(img.getSavedName());
        imageRepository.delete(img);
        log.info("deleteImage: deleted Image[id={}]", id);
    }

    @Override
    @Cacheable(value = "imageCache", key = "#entityType + '-' + #entityId")
    public List<ImageDTO> getImagesByEntity(EntityType entityType, Integer entityId) throws Exception {
        log.debug("getImagesByEntity: validating entity {}#{}", entityType, entityId);
        validateEntityExists(entityType, entityId);

        return switch (entityType) {
            case USER -> {
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.USER, entityId)));
                if (!(authService.isOwnerOrAdmin(user.getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                yield user.getImage() != null ? List.of(convertToDTO(user.getImage())) : List.of();
            }
            case PRODUCT -> {
                Product product = productRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.PRODUCT, entityId)));
                if (!(authService.isOwnerOrAdmin(product.getFarmer().getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                yield product.getImages().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
            case FARMER -> {
                Farmer farmer = farmerRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.FARMER, entityId)));
                if (!(authService.isOwnerOrAdmin(farmer.getUser().getId())))
                    throw new AccessDeniedException(Constants.ACCESS_DENIED);
                yield farmer.getImages().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
            }
        };
    }

    @Override
    @Cacheable(value = "imageCache", key = "#id")
    public ImageDTO getImageById(Integer id) throws Exception {
        Image img = imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(Constants.RESOURCE_NOT_FOUND, Constants.IMAGE, id)));
        return convertToDTO(img);
    }

    @Override
    public Page<ImageDTO> getImagesByEntityPaged(EntityType entityType, Integer entityId, PaginationRequest req) throws Exception {
        log.debug("getImagesByEntityPaged: validating entity {}#{}", entityType, entityId);
        validateEntityExists(entityType, entityId);

        Pageable pageReq = req.toPageable();

        return switch (entityType) {
            case USER -> {
                User user = userRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + entityId));
                if (!(authService.isOwnerOrAdmin(user.getId())))
                    throw new AccessDeniedException("Access denied");
                if (user.getImage() != null) {
                    yield new PageImpl<>(List.of(convertToDTO(user.getImage())), pageReq, 1);
                }
                yield new PageImpl<>(List.of(), pageReq, 0);
            }
            case PRODUCT -> {
                Product product = productRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + entityId));
                if (!(authService.isOwnerOrAdmin(product.getFarmer().getUser().getId())))
                    throw new AccessDeniedException("Access denied");
                List<ImageDTO> imageDtos = product.getImages().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                yield new PageImpl<>(imageDtos, pageReq, imageDtos.size());
            }
            case FARMER -> {
                Farmer farmer = farmerRepository.findById(entityId)
                        .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + entityId));
                if (!(authService.isOwnerOrAdmin(farmer.getUser().getId())))
                    throw new AccessDeniedException("Access denied");
                List<ImageDTO> imageDtos = farmer.getImages().stream()
                        .map(this::convertToDTO)
                        .collect(Collectors.toList());
                yield new PageImpl<>(imageDtos, pageReq, imageDtos.size());
            }
        };
    }

    private void validateEntityExists(EntityType entityType, Integer entityId) throws Exception {
        switch (entityType) {
            case USER -> userRepository.findById(entityId)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + entityId));
            case PRODUCT -> productRepository.findById(entityId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + entityId));
            case FARMER -> farmerRepository.findById(entityId)
                    .orElseThrow(() -> new ResourceNotFoundException("Farmer not found with ID: " + entityId));
            default -> throw new IllegalArgumentException("Unsupported entity type: " + entityType);
        }
    }

    private int getMaxImagesForEntityType(EntityType entityType) {
        return switch (entityType) {
            case FARMER -> MAX_IMAGES_FARMER;
            case PRODUCT -> MAX_IMAGES_PRODUCT;
            default -> throw new IllegalArgumentException("Unsupported entity type for multiple images: " + entityType);
        };
    }


    @Override
    @Cacheable(value = "imageCache", key = "'all'")
    public List<ImageDTO> getAllImages() {
        return imageRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "imageCache", key = "#fileType.name()")
    public List<ImageDTO> getImagesByFileType(FileType fileType) {
        return imageRepository.findByFileType(fileType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ImageDTO> searchImages(EntityType entityType, Integer entityId, FileType fileType,
                                       Long minSize, Long maxSize, String fileName) {
        return imageRepository
                .searchImages(entityType, entityId, fileType, minSize, maxSize, fileName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ImageDTO> getAllImagesPaged(PaginationRequest req) {
        Pageable pageReq = req.toPageable();
        return imageRepository.findAll(pageReq)
                .map(this::convertToDTO);
    }

    @Override
    public Page<ImageDTO> searchImagesPaged(PaginationRequest req, EntityType entityType, Integer entityId,
                                            FileType fileType, Long minSize, Long maxSize, String fileName) {
        Pageable pageReq = req.toPageable();
        return imageRepository
                .searchImagesPaged(entityType, entityId, fileType, minSize, maxSize, fileName, pageReq)
                .map(this::convertToDTO);
    }

    private ImageDTO convertToDTO(Image img) {
        return modelMapper.map(img, ImageDTO.class);
    }
}