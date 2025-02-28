package com.springboot.manage_users.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.springboot.manage_users.dto.DocumentResponse;
import com.springboot.manage_users.entity.Documents;
import com.springboot.manage_users.service.DocumentsService;

class DocumentsControllerTest {

    @Mock
    private DocumentsService docsService;

    @InjectMocks
    private DocumentsController documentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUploadFiles() {
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "Hello World".getBytes());
        String userId = UUID.randomUUID().toString();
        Documents document = new Documents();
        document.setId(UUID.randomUUID().toString());
        when(docsService.uploadDocuments(any(MultipartFile.class), eq(userId))).thenReturn(document);
        
        ResponseEntity<Map<String, Object>> response = documentsController.uploadFiles(new MultipartFile[]{file}, userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().containsKey("docIds"));
    }

    @Test
    void testGetDocuments() {
        String docId = UUID.randomUUID().toString();
        Documents document = new Documents();
        document.setId(docId);
        document.setFilename("test.txt");
        document.setFiletype("text/plain");
        document.setFiledata("Hello World".getBytes());
        when(docsService.getDocuments(docId)).thenReturn(Optional.of(document));
        
        ResponseEntity<byte[]> response = documentsController.getDocuments(docId, false);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetUserDocuments() {
        String userId = UUID.randomUUID().toString();
        Documents document = new Documents();
        document.setId(UUID.randomUUID().toString());
        document.setFilename("test.txt");
        document.setFiletype("text/plain");
        document.setFilesize("12");
        when(docsService.getUserDocuments(userId)).thenReturn(List.of(document));
        
        ResponseEntity<?> response = documentsController.getUserDocuments(userId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteDocument() {
        String docId = UUID.randomUUID().toString();
        doNothing().when(docsService).deleteDocument(docId);
        
        ResponseEntity<String> response = documentsController.deleteDocument(docId);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("File deleted successfully", response.getBody());
    }
}
