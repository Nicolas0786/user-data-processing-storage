package com.example.userdataprocessingstorage.parser;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.model.UserInput;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
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

    //Todo não esquecer de mover para um pacote....
    @Getter
    @Setter
    @JacksonXmlRootElement(localName = "users")
    public static class UsersXml {
        @JacksonXmlElementWrapper(useWrapping = false)
        @JacksonXmlProperty(localName = "user")
        private List<UserInput> users;
    }
}
