package com.shandakova.documents.controllers;

import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.dto.DocumentTypeDTO;
import com.shandakova.documents.services.DocumentTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;

@RequestMapping("/type")
@Tag(name = "Document type controller", description = "Работа с типами документов")
@Controller
public class DocumentTypeController {
    final DocumentTypeService documentTypeService;

    public DocumentTypeController(DocumentTypeService documentTypeService) {
        this.documentTypeService = documentTypeService;
    }

    @Operation(description = "Получить все типы документов")
    @GetMapping(value = "/all", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "An error has occurred",
                    content = @Content)})
    public @ResponseBody
    List<DocumentTypeDTO> getAllTypes() throws SQLException {
        return documentTypeService.getAll();
    }
}
