package com.example.userdataprocessingstorage.service;

import com.example.userdataprocessingstorage.enums.FileType;
import jakarta.validation.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserFileValidatorService {

    public FileType validate(MultipartFile multipartFile, String type) {
        FileType fileType;

        try {
            fileType = FileType.from(type);
        } catch (Exception e) {
            throw new ValidationException("Parâmetro 'type' deve ser csv, json ou xml");
        }

        if (multipartFile == null || multipartFile.isEmpty()) throw new ValidationException("Arquivo vazio");
        String filename = Optional.ofNullable(multipartFile.getOriginalFilename()).orElse("");
        String expectedExt = "." + fileType.name().toLowerCase();
        if (!filename.toLowerCase().endsWith(expectedExt)) {
            throw new ValidationException("Extensão do arquivo não confere com o tipo informado (" + expectedExt + ").");
        }
        return fileType;
    }
}
