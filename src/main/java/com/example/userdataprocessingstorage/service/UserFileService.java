package com.example.userdataprocessingstorage.service;

import com.example.userdataprocessingstorage.dto.response.UserOutput;
import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.model.User;
import com.example.userdataprocessingstorage.dto.request.UserInput;
import com.example.userdataprocessingstorage.dto.response.UsersResponse;
import com.example.userdataprocessingstorage.parser.UserFileParser;
import com.example.userdataprocessingstorage.repository.UserRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFileService {

    private final UserRepository repository;

    private final Map<FileType, UserFileParser> userFileParserMap;

    private final Validator validator;

    public UserFileService(List<UserFileParser> parsers, UserRepository userRepository, Validator validator) {
        this.repository = userRepository;
        this.userFileParserMap = parsers.stream().collect(Collectors.toMap(UserFileParser::fileSupports, p -> p));
        this.validator = validator;
    }

    @Transactional
    public int processUpload(MultipartFile multipartFile, FileType fileType) {
        UserFileParser userFileParser = userFileParserMap.get(fileType);
        List<UserInput> userInputs;

        try {
            userInputs = userFileParser.parse(multipartFile.getInputStream());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error ao fazer o parse para " + fileType.name(), e);
        }

        if (userInputs == null || userInputs.isEmpty()) throw new ValidationException("Nenhum registro valido encontrado.");
        List<User> toSave = new ArrayList<>();
        for (UserInput userInput: userInputs) {
            Set<ConstraintViolation<UserInput>> validate = validator.validate(userInput);
            if (!validate.isEmpty()) {
                String msg = validate.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));
                throw new ValidationException(msg);
            }

            User user = new User();
            user.setName(userInput.getName());
            user.setEmail(userInput.getEmail());
            user.setSource(fileType);
            toSave.add(user);
        }
        this.repository.saveAll(toSave);
        return toSave.size();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findFileByFormat(String format)  {
        List<User> users = this.repository.findAll();

        List<UserOutput> userOutputs = users.stream().map(UserOutput::toOutput).toList();

        return switch (format.toLowerCase()) {
            case "json" -> ResponseEntity.ok(new UsersResponse(userOutputs));
            case "xml" -> ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(new UsersResponse(userOutputs));
            case "csv" -> this.toCsvResponse(userOutputs);
            default -> throw  new ValidationException("Este formato é inválido: use JSON, XML ou CSV");
        };

    }

    private ResponseEntity<byte[]> toCsvResponse(List<UserOutput> userOutputs) {
        StringWriter writer = new StringWriter();

        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                .setHeader("id", "name", "email", "source").build())) {
            for (UserOutput userOutput: userOutputs) {
                printer.printRecord(userOutput.id(), userOutput.email(), userOutput.email(), userOutput.source());
            }
         } catch (IOException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = writer.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"users.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(bytes);
    }

}
