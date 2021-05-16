package com.shandakova.documents.controllers;

import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.services.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@Tag(name = "Document Controller", description = "Работа с документами")
@Controller
@RequestMapping("/document")
public class DocumentController {
    @Autowired
    DocumentService documentService;

    @Operation(description = "Создать документ")
    @PostMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created document",
                    content = {@Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<String> createDocument(@RequestBody DocumentDTO documentDTO) {
        try {
            documentService.create(documentDTO);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>("Created document", HttpStatus.CREATED);
    }

    @Operation(description = "Создать новую версию документа")
    @PostMapping("/{id}/new-version")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new version of document",
                    content = {@Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<String> createNewVersion(@RequestBody DocumentDTO documentDTO,
                                                   @PathVariable("id") Integer id) {
        try {
            documentDTO.setId(id);
            documentService.createNewVersion(documentDTO);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<>("Created new version of document", HttpStatus.CREATED);
    }

    @Operation(description = "Найти все версии документа")
    @GetMapping(value = "/{id}/versions", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class)))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<List<DocumentDTO>> getVersionOfDocument(@PathVariable("id") Integer id) {
        List<DocumentDTO> versions;
        try {
            versions = documentService.getAllVersionOfDocumentWithId(id);
            return new ResponseEntity<>(versions, HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
