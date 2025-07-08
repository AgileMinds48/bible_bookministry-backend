package com.evbooksministry.bibleandbookministry.dtos;

import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public record UpdateBookMedia(
        UUID bookId,
        MultipartFile [] bookMedia
) {
}
