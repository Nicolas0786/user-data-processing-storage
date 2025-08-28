package com.example.userdataprocessingstorage.parser;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.dto.request.UserInput;

import java.io.InputStream;
import java.util.List;

public interface UserFileParser {
    FileType fileSupports();
    List<UserInput> parse(InputStream inputStream) throws Exception;
}
