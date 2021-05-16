package com.shandakova.documents.controllers;

import com.shandakova.documents.dto.DocumentDTO;
import com.shandakova.documents.dto.NodeDTO;
import com.shandakova.documents.services.NodeService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/node")
@Tag(name = "Node controller", description = "Работа с узлами")
@Controller
public class NodeController {
    final NodeService nodeService;

    public NodeController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @Operation(description = "Получить все узлы по поиску")
    @GetMapping(value = "/all/find", produces = "application/json;charset=UTF-8")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = DocumentDTO.class)))}),
            @ApiResponse(responseCode = "500", description = "An error has occurred",
                    content = @Content)})
    public @ResponseBody
    List<NodeDTO> getAllNodes(@RequestParam(required = false) String parent,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) String type) throws SQLException {
        Map<String, String> query = new HashMap<>();
        if (parent != null) {
            query.put("parent-id", parent);
        }
        if (name != null) {
            query.put("name", name);
        }
        if (type != null) {
            query.put("type", type);
        }
        return nodeService.getListByQuery(query);
    }
}
