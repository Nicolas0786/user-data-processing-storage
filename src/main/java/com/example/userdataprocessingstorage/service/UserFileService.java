package com.example.userdataprocessingstorage.service;

import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.model.User;
import com.example.userdataprocessingstorage.model.UserInput;
import com.example.userdataprocessingstorage.model.UsersResponse;
import com.example.userdataprocessingstorage.parser.UserFileParser;
import com.example.userdataprocessingstorage.repository.UserRepository;
import jakarta.validation.ValidationException;
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
import java.util.stream.Collectors;

@Service
public class UserFileService {

    private final UserRepository repository;

    private final Map<FileType, UserFileParser> userFileParserMap;

    public UserFileService(List<UserFileParser> parsers, UserRepository userRepository) {
        this.repository = userRepository;
        this.userFileParserMap = parsers.stream().collect(Collectors.toMap(UserFileParser::fileSupports, p -> p));
    }

    @Transactional
    public int processUpload(MultipartFile multipartFile, FileType fileType) throws Exception {
        if (multipartFile == null || multipartFile.isEmpty()) throw new ValidationException("Arquivo vazio");

        UserFileParser userFileParser = userFileParserMap.get(fileType);
        if (userFileParser == null) throw new IllegalArgumentException("Tipo não suportado: " + fileType);

        String nomeArquivo = multipartFile.getOriginalFilename();
        if (nomeArquivo != null && !nomeArquivo.toUpperCase().endsWith(fileType.name())) throw new ValidationException("O tipo do arquivo não condiz com oque foi passado.");

        List<UserInput> userInputs = userFileParser.parse(multipartFile.getInputStream());

        if (userInputs == null || userInputs.isEmpty()) throw new ValidationException("Nenhum registro valido encontrado.");

        List<User> toSave = new ArrayList<>();
        for (UserInput userInput: userInputs) {

            this.validate(userInput);
            User user = new User();

            user.setName(userInput.getName());
            user.setEmail(userInput.getEmail());
            user.setSource(fileType.name());
            toSave.add(user);

        }
        this.repository.saveAll(toSave);
        return toSave.size();
    }


    private void validate(UserInput userInput) {
        if (userInput == null) throw new ValidationException("Registro invalido");
        if (userInput.getName() == null || userInput.getName().isBlank()) throw new ValidationException("Name obrigatorio.");
        if (userInput.getEmail() == null || userInput.getEmail().isBlank()) throw new ValidationException("Email obrigatorio.");
        if (!userInput.getEmail().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) throw new ValidationException("Email inválido: " + userInput.getEmail());
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> findFileByFormat(String format) throws IOException {
        List<User> users = this.repository.findAll();

        return switch (format.toLowerCase()) {
            case "json" -> ResponseEntity.ok(new UsersResponse(users));
            case "xml" -> ResponseEntity.ok().contentType(MediaType.APPLICATION_XML).body(new UsersResponse(users));
            case "csv" -> this.toCsvResponse(users);
            default -> throw  new ValidationException("Este formato é inválido: use JSON, XML ou CSV");
        };

    }

    private ResponseEntity<byte[]> toCsvResponse(List<User> users) throws IOException {
        StringWriter writer = new StringWriter();

        try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.DEFAULT.builder()
                .setHeader("id", "name", "email", "source").build())) {
            for (User user: users) {
                printer.printRecord(user.getId(), user.getName(), user.getEmail(), user.getSource());
            }
         }
        byte[] bytes = writer.toString().getBytes(StandardCharsets.UTF_8);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"users.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=UTF-8"))
                .body(bytes);
    }

}
