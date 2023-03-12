package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.UploadCommand;

public interface Walker {
    void upload(UploadCommand command);
}
