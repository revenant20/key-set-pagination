package com.evilcorp.keysetpagination.service;

import com.evilcorp.keysetpagination.dto.UploadCommand;

public interface Walker {
    void walk(UploadCommand command);
    String getPath();
    String getName();
}
