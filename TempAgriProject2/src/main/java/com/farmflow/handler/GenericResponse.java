package com.farmflow.handler;

import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GenericResponse<T> {
    private HttpStatus responseStatus;
    private String status;
    private String message;
    private T data;

    public ResponseEntity<Map<String, Object>> create(){
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("timestamp", LocalDateTime.now());
        map.put("status", status);
        map.put("message", message);

        if (!ObjectUtils.isEmpty(data)) {
            map.put("data", data);
        }
        return new ResponseEntity<>(map, responseStatus);
    }
}

