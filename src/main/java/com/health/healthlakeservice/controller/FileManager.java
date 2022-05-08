package com.health.healthlakeservice.controller;

import com.health.healthlakeservice.service.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@SecurityRequirement(name = "capstone")
/**
 * NLP File Upload Service Controller
 */
public class FileManager {

    @Autowired
    FileService fileService;

    @GetMapping("/files")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * List of Files endpoint
     */
    public ResponseEntity<List<String>> getListOfFiles() {
        return new ResponseEntity<>(fileService.listFiles(), HttpStatus.OK);
    }

    @PostMapping("/upload")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * File Upload endpoint
     */
    public ResponseEntity<String> uploadFile(@RequestParam("name") String name,
                                     @RequestParam("file") MultipartFile file) throws IOException {

        fileService.uploadFile(name, file);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/download/{name}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * File Download endpoint
     */
    public ResponseEntity<byte[]> downloadFile(@PathVariable String name) throws IOException {
        ByteArrayOutputStream downloadInputStream = fileService.downloadFile(name);

        return ResponseEntity.ok()
                .contentType(contentType(name))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + name + "\"")
                .body(downloadInputStream.toByteArray());
    }

    @GetMapping(value = "/delete/{name}")
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    /**
     * File Delete endpoint
     */
    public ResponseEntity<String> deleteFile(@PathVariable("name") String name) {

        fileService.deleteFile(name);
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    /**
     * Determine File type
     * @param filename
     * @return
     */
    private MediaType contentType(String filename) {
        return MediaType.APPLICATION_OCTET_STREAM;
    }
}
