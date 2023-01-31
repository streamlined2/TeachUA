package com.softserve.teachua.dto.certificateTemplate;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificateTemplateLastModificationDateSavingResponse {
    private List<String> messages;
    private String filePath;
}
