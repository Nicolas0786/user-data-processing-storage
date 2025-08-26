package com.example.userdataprocessingstorage.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.model.UserInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

@Component
public class JsonUserFileParser implements UserFileParser{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public FileType fileSupports() {
        return FileType.JSON;
    }

    @Override
    public List<UserInput> parse(InputStream inputStream) throws Exception {

        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}
