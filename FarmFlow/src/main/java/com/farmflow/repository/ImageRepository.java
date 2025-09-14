package com.farmflow.repository;

import com.farmflow.entity.Image;
import com.farmflow.enums.EntityType;
import com.farmflow.enums.FileType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByEntityTypeAndEntityId(EntityType entityType, Integer entityId);
    Page<Image> findByEntityTypeAndEntityId(EntityType entityType, Integer entityId, Pageable pageable);

    List<Image> findByFileType(FileType fileType);
    Page<Image> findByFileType(FileType fileType, Pageable pageable);

    // 2) “Search” by optional filters: entityType, entityId, fileType, size range, fileName like
    @Query("""
        select i from Image i
        where  (:entityType is null or i.entityType = :entityType)
          and  (:entityId   is null or i.entityId   = :entityId)
          and  (:fileType   is null or i.fileType   = :fileType)
          and  (:minSize    is null or i.fileSize  >= :minSize)
          and  (:maxSize    is null or i.fileSize  <= :maxSize)
          and  (:fileName   is null or lower(i.fileName) like lower(concat('%',:fileName,'%')))
        """)
    List<Image> searchImages(
            @Param("entityType") EntityType entityType,
            @Param("entityId")   Integer entityId,
            @Param("fileType")   FileType fileType,
            @Param("minSize")    Long minSize,
            @Param("maxSize")    Long maxSize,
            @Param("fileName")   String fileName
    );

    @Query("""
        select i from Image i
        where  (:entityType is null or i.entityType = :entityType)
          and  (:entityId   is null or i.entityId   = :entityId)
          and  (:fileType   is null or i.fileType   = :fileType)
          and  (:minSize    is null or i.fileSize  >= :minSize)
          and  (:maxSize    is null or i.fileSize  <= :maxSize)
          and  (:fileName   is null or lower(i.fileName) like lower(concat('%',:fileName,'%')))
        """)
    Page<Image> searchImagesPaged(
            @Param("entityType") EntityType entityType,
            @Param("entityId")   Integer entityId,
            @Param("fileType") FileType fileType,
            @Param("minSize")    Long minSize,
            @Param("maxSize")    Long maxSize,
            @Param("fileName")   String fileName,
            Pageable pageable
    );
}

