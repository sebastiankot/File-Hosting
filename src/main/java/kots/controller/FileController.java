package kots.controller;

import kots.controller.dto.FileDownloadDto;
import kots.controller.dto.FileMetadataDto;
import kots.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<FileMetadataDto> uploadFile(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.store(file));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Resource> getFile(@PathVariable long id) {
        FileDownloadDto fileDto = fileService.getFile(id);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getName() + "\"")
                .body(fileDto.getFileResource());
    }

    @GetMapping
    public ResponseEntity<List<FileMetadataDto>> getFiles() {
        return ResponseEntity.ok(fileService.getFiles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFile(@PathVariable long id) {
        fileService.deleteFile(id);
        return ResponseEntity.noContent().build();
    }
}
