package com.example.userdataprocessingstorage.controller;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.service.UserFileService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-file")
public class UserFileController {

    private final UserFileService userFileService;


    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("type") String type, @RequestParam("file") MultipartFile multipartFile) throws Exception {

        FileType fileType;

        try { fileType = FileType.exists(type); }
        catch (Exception e) { throw new ValidationException("Parâmetro 'type' deve ser csv, json ou xml"); }

        int count = this.userFileService.processUpload(multipartFile, fileType);

        return ResponseEntity.ok(new UploadResult("OK", fileType.name(), count));
    }

    public record UploadResult(String status, String source, int saved) {}

    @GetMapping
    public ResponseEntity<?> find(@RequestParam(value = "format", required = false, defaultValue = "json") String format) throws IOException {
        return this.userFileService.findFileByFormat(format);
    }

}
