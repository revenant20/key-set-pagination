package com.evilcorp.keysetpagination.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadCommand {
    int pageSize;
}
