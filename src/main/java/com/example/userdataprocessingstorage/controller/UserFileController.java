package com.example.userdataprocessingstorage.controller;

import com.example.userdataprocessingstorage.dto.response.UploadResult;
import com.example.userdataprocessingstorage.enums.FileType;
import com.example.userdataprocessingstorage.service.UserFileService;
import com.example.userdataprocessingstorage.service.UserFileValidatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user-file")
public class UserFileController {

    private final UserFileService userFileService;

    private final UserFileValidatorService userFileValidatorService;


    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(tags = "User File", summary = "Upload de aquivos de usuários",
            description = "Aceita arquivos no formato CSV, JSON ou XML contendo {name, email}.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Processado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Parâmetros ou arquivo inválido"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    public ResponseEntity<?> upload(
            @Parameter(name = "type", description = "Tipo do arquivo CSV, JSON ou XML", required = true, example = "json")
            @RequestParam("type") String type,
            @Parameter(name = "file", description = "Arquivo a ser enviado", required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(type = "string", format = "binary")))
            @RequestParam("file") MultipartFile multipartFile) {
        FileType fileType = this.userFileValidatorService.validate(multipartFile, type);
        int count = this.userFileService.processUpload(multipartFile, fileType);
        return ResponseEntity.ok(new UploadResult("OK", fileType.name(), count));
    }

    @GetMapping
    @Operation(tags = "User File", summary = "Listar usuários",
            description = "Lista todos os usuários. O formato de saída pode ser json, xml ou csv via query param.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Formato inválido")
    })
    public ResponseEntity<?> find(@Parameter(name = "format", description = "Formato de saída: json (default) | xml | csv")
            @RequestParam(value = "format", required = false, defaultValue = "json") String format) {
        return this.userFileService.findFileByFormat(format);
    }

}
