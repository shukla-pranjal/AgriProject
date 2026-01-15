package com.farmflow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class EmailRequest {
    private String title;
    private String subject;
    private String body;
    private String recipientEmail;
    private boolean isHtml;
    private String templateName; // New field
    private Map<String, Object> contextVariables;
}
