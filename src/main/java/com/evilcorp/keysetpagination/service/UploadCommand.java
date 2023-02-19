package com.evilcorp.keysetpagination.service;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UploadCommand {
    int pageSize;
}
