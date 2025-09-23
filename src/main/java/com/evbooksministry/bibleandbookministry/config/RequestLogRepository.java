package com.evbooksministry.bibleandbookministry.config;

import com.evbooksministry.bibleandbookministry.models.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RequestLogRepository extends JpaRepository<RequestLog, UUID> {
}
