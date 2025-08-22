package com.evbooksministry.bibleandbookministry.dtos;

import java.sql.Timestamp;
import java.util.UUID;

public record CategoryDTO(
        UUID categoryId,
        String categoryName,
        String categoryDescription,
        Timestamp createdOn,
        Timestamp updatedOn
) {}
