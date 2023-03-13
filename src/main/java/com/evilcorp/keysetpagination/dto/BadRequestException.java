package com.evilcorp.keysetpagination.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Задан неверный токен или фильтр")
public class BadRequestException extends RuntimeException {
}
