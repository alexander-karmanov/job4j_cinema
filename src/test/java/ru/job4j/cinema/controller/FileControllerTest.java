package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import ru.job4j.cinema.service.FileService;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.dto.FileDto;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import static org.assertj.core.api.Assertions.*;
import org.springframework.http.HttpStatus;

public class FileControllerTest {
    private FileService fileService;

    private FileController fileController;

    @BeforeEach
    void init() {
        fileService = mock(FileService.class);
        fileController = new FileController(fileService);
    }

    @Test
    void whenFileExistsThenReturnOkResponseWithContent() {
        var fileContent = new byte[]{1, 2, 3, 4, 5};
        var fileDto = new FileDto("testFile", fileContent);
        when(fileService.findById(1)).thenReturn(Optional.of(fileDto));
        ResponseEntity<?> response = fileController.getById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(fileContent);
    }

    @Test
    void whenFileDoesNotExistThenReturnNotFoundResponse() {
        when(fileService.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<?> response = fileController.getById(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
    }
}
