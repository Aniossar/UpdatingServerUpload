package com.CalculatorMVCUpload.service;

import com.CalculatorMVCUpload.entity.UploadedFile;
import com.CalculatorMVCUpload.repository.UploadFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UploadFileService {

    @Autowired
    private UploadFileRepository uploadFileRepository;

    public List<UploadedFile> getAllFiles() {
        return uploadFileRepository.getAllFiles();
    }

    public UploadedFile getFileViaId(int id){
        return uploadFileRepository.getFileViaId(id);
    }


    public void addNewFile(UploadedFile uploadedFile) {
        uploadFileRepository.addNewFile(uploadedFile);
    }

    public UploadedFile getLastFile() {
        return uploadFileRepository.getLastFile();
    }


    public void deleteFile(int id) {
        uploadFileRepository.deleteFile(id);
    }
}
