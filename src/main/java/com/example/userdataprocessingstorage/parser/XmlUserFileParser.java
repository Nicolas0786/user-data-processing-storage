package com.example.userdataprocessingstorage.parser;

import com.example.userdataprocessingstorage.dto.request.UserInput;
import com.example.userdataprocessingstorage.dto.xml.UsersXml;
import com.example.userdataprocessingstorage.enums.FileType;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
public class XmlUserFileParser implements UserFileParser {

    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public FileType fileSupports() {
        return FileType.XML;
    }

    @Override
    public List<UserInput> parse(InputStream inputStream) throws Exception {
        UsersXml wrapper = xmlMapper.readValue(inputStream, UsersXml.class);
        return wrapper.getUsers();
    }

}
