/*
package com.evbooksministry.bibleandbookministry;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.evbooksministry.bibleandbookministry.services.CloudinaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CloudinaryServiceTest {
    @Mock
    private Cloudinary cloudinary;
    @Mock
    private Uploader uploader;
    @Mock
    private MultipartFile multipartFile;

    @InjectMocks
    private CloudinaryService cloudinaryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cloudinaryService = new CloudinaryService(cloudinary);
    }

    */
/**
     * Tests successful file upload to Cloudinary.
     * Verifies that the returned URL matches the expected value.
     *//*

    @Test
    void testUploadFile_Success() throws Exception {
        byte[] fileBytes = new byte[]{1, 2, 3};
        when(multipartFile.getBytes()).thenReturn(fileBytes);
        when(cloudinary.uploader()).thenReturn(uploader);
        Map<String, Object> uploadResult = new HashMap<>();
        uploadResult.put("url", "http://cloudinary.com/test.jpg");
        when(uploader.upload(fileBytes, com.cloudinary.utils.ObjectUtils.emptyMap())).thenReturn(uploadResult);

        String result = cloudinaryService.uploadFile(multipartFile);
        assertEquals("http://cloudinary.com/test.jpg", result);
    }

    */
/**
     * Tests file upload when an exception is thrown.
     * Verifies that the method returns null and exception is handled.
     *//*

    @Test
    void testUploadFile_Exception() throws Exception {
        when(multipartFile.getBytes()).thenThrow(new RuntimeException("File error"));
        String result = cloudinaryService.uploadFile(multipartFile);
        assertNull(result);
    }
} */
