package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.exception.BadNamingException;
import com.CalculatorMVCUpload.exception.FileStorageException;
import com.CalculatorMVCUpload.exception.FileNotFoundException;
import com.CalculatorMVCUpload.property.FileStorageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private String fileNamePrefix = "KPD_";
    private String fileNameSuffix = "zip";

    private Path fileStorageLocation;

    public Path getFileStorageLocation() {
        return fileStorageLocation;
    }

    @Autowired
    public FileStorageService(FileStorageProperties fileStorageProperties) {

        String fileUploadDir = new File("").getAbsolutePath() + fileStorageProperties.getUploadDir();
        this.fileStorageLocation = Paths.get(fileUploadDir);

        try {
            Files.createDirectories(this.fileStorageLocation);
            System.out.println("Files will be stored in directory: " + fileStorageLocation.toAbsolutePath().normalize());
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            if (fileName.contains("..")) {
                throw new BadNamingException("Filename contains invalid path sequence " + fileName);
            }

            if (!fileName.substring(0,4).equals(fileNamePrefix)||
                    !fileName.substring(fileName.length()-3).equals(fileNameSuffix)){
                throw new BadNamingException("Filename should be like 'KPD_*.zip'. Rename the file " + fileName);
            }

            fileName = StringUtils.cleanPath(file.hashCode() + "_" + file.getOriginalFilename());

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileNotFoundException("File not found " + fileName, ex);
        }
    }
}