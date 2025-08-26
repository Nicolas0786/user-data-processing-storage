package com.example.userdataprocessingstorage.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-file")
public class UserFileController {

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("type") String type, @RequestParam("file")MultipartFile file) {

        return null;

    }

}
