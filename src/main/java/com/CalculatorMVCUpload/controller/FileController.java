package com.CalculatorMVCUpload.controller;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.payload.UploadFileResponse;
import com.CalculatorMVCUpload.service.FileStorageService;
import com.CalculatorMVCUpload.service.UploadFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UploadFileService uploadFileService;

    @GetMapping("/allFiles")
    public List<UploadedFile> getAllFiles() {
        List<UploadedFile> allFiles = uploadFileService.getAllFiles();
        return allFiles;
    }

    @GetMapping("/getFile/{id}")
    public UploadedFile getFileViaId(@PathVariable int id) {
        UploadedFile uploadedFile = uploadFileService.getFileViaId(id);
        return uploadedFile;
    }

    @PostMapping("/uploadFile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileStorageService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        Date dateNow = new Date();

        UploadedFile uploadedFile = new UploadedFile(
                fileName,
                fileStorageService.getFileStorageLocation().toAbsolutePath().normalize() + "\\" + fileName,
                fileDownloadUri,
                dateNow,
                file.getSize(),
                file.hashCode());

        uploadFileService.addNewFile(uploadedFile);

        return new UploadFileResponse(fileName, fileDownloadUri,
                file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadMultipleFiles")
    public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        return Arrays.asList(files)
                .stream()
                .map(file -> uploadFile(file))
                .collect(Collectors.toList());
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/deleteFile/{id}")
    public void deleteFile(@PathVariable int id) {
        UploadedFile uploadedFile = uploadFileService.getFileViaId(id);
        Resource resource = fileStorageService.loadFileAsResource(uploadedFile.getName());
        try {
            boolean deleteResult = Files.deleteIfExists(Paths.get(resource.getFile().getAbsolutePath()));
            if (deleteResult) {
                uploadFileService.deleteFile(id);
            }
        } catch (Exception e) {
            throw new FileNotFoundException("File not found " + uploadedFile.getName());
        }
    }


    @GetMapping("/lastFile")
    public UploadedFile getLastUploadedFile() {
        try {
            UploadedFile lastFile = uploadFileService.getLastFile();
            return lastFile;
        } catch (Exception e) {
            throw new FileNotFoundException("No files uploaded");
        }
    }

}
