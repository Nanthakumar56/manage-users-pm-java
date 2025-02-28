package com.springboot.manage_users.service;

import com.springboot.manage_users.entity.Documents;
import com.springboot.manage_users.repository.DocumentsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DocumentsServiceTest {

    @Mock
    private DocumentsRepository docsRepository; // Mock for DocumentsRepository

    @InjectMocks
    private DocumentsService documentsService; // Inject mocks into DocumentsService

    private MultipartFile mockFile;
    private Documents mockDocument;

    @BeforeEach
    public void setUp() {
        // Create a mock MultipartFile
        mockFile = new MockMultipartFile(
                "testFile",
                "testFile.pdf",
                "application/pdf",
                "Test file content".getBytes()
        );

        // Create a mock Documents object
        mockDocument = new Documents();
        mockDocument.setId(UUID.randomUUID().toString());
        mockDocument.setUserid("user123");
        mockDocument.setFilename("testFile.pdf");
        mockDocument.setFiletype("application/pdf");
        mockDocument.setFilesize("12345");
        mockDocument.setFiledata("Test file content".getBytes());
        mockDocument.setCreatedat(LocalDateTime.now());
    }

    @Test
    public void testUploadDocuments() throws IOException {
        // Arrange
        when(docsRepository.save(any(Documents.class))).thenReturn(mockDocument);

        // Act
        Documents result = documentsService.uploadDocuments(mockFile, "user123");

        // Assert
        assertNotNull(result);
        assertEquals("user123", result.getUserid());
        assertEquals("testFile.pdf", result.getFilename());
        verify(docsRepository, times(1)).save(any(Documents.class));
    }

    @Test
    public void testGetDocuments() {
        // Arrange
        String documentId = mockDocument.getId();
        when(docsRepository.findById(documentId)).thenReturn(Optional.of(mockDocument));

        // Act
        Optional<Documents> result = documentsService.getDocuments(documentId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(documentId, result.get().getId());
        verify(docsRepository, times(1)).findById(documentId);
    }

    @Test
    public void testGetUserDocuments() {
        // Arrange
        String userId = "user123";
        when(docsRepository.findByUserId(userId)).thenReturn(Arrays.asList(mockDocument));

        // Act
        List<Documents> result = documentsService.getUserDocuments(userId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(userId, result.get(0).getUserid());
        verify(docsRepository, times(1)).findByUserId(userId);
    }

    @Test
    public void testDeleteDocument() {
        // Arrange
        String documentId = mockDocument.getId();
        when(docsRepository.findById(documentId)).thenReturn(Optional.of(mockDocument));

        // Act
        documentsService.deleteDocument(documentId);

        // Assert
        verify(docsRepository, times(1)).findById(documentId);
        verify(docsRepository, times(1)).deleteById(documentId);
    }

    @Test
    public void testDeleteDocument_NotFound() {
        // Arrange
        String documentId = "nonExistentId";
        when(docsRepository.findById(documentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            documentsService.deleteDocument(documentId);
        });

        assertEquals("Document not found with id " + documentId, exception.getMessage());
        verify(docsRepository, times(1)).findById(documentId);
        verify(docsRepository, never()).deleteById(any());
    }
}