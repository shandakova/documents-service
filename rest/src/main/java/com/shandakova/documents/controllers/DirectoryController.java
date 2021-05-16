package com.shandakova.documents.controllers;

import com.shandakova.documents.dto.DirectoryDTO;
import com.shandakova.documents.services.DirectoriesService;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name = "Directory Controller", description = "Работа с каталогами")
@Controller
@RequestMapping("/directory")
public class DirectoryController {
    @Autowired
    private DirectoriesService directoriesService;

    @Operation(description = "Создать каталог")
    @PostMapping("/")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created directory",
                    content = {@Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<String> createDirectory(@RequestBody DirectoryDTO directoryDTO) {
        try {
            directoriesService.create(directoryDTO);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<String>("Created directory", HttpStatus.CREATED);
    }

    @Operation(description = "Создать несколько каталогов")
    @PostMapping("/all")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created directories",
                    content = {@Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<String> creatManyDirectory(@RequestBody List<DirectoryDTO> directoryDTO) {
        try {
            directoriesService.createMany(directoryDTO);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<String>("Created directories", HttpStatus.CREATED);
    }

    @Operation(description = "Обновить каталог по id")
    @PutMapping("/{id}")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Directory updated",
                    content = {@Content(mediaType = "text/plain; charset=utf-8",
                            schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "An error has occurred",
                    content = @Content)})
    public ResponseEntity<String> updateById(@RequestBody DirectoryDTO directoryDTO,
                                             @PathVariable("id") Integer id) {
        try {
            directoryDTO.setId(id);
            directoriesService.update(directoryDTO);
        } catch (SQLException e) {
            return ResponseEntity.badRequest().build();
        }
        return new ResponseEntity<String>("Updated directory", HttpStatus.OK);
    }

}
