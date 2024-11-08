package com.goose.calculator.controller;

import com.goose.calculator.config.RuntimeMSG;
import com.goose.calculator.dto.AirplaneDTO;
import com.goose.calculator.service.AirplaneService;
import com.goose.calculator.model.Airplane;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Airplanes")
@RestController
@RequestMapping("airplanes")
@RequiredArgsConstructor
public class AirplaneController {

    private final AirplaneService airplaneService;

    @Operation(summary = "Fetch all Airplanes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = AirplaneDTO[].class))),
    })
    @GetMapping
    public List<AirplaneDTO> getAirplanes() {
        return airplaneService.getAirplanes();
    }

    @Operation(summary = "Fetch airplane")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = Airplane.class))),
            @ApiResponse(responseCode = "404", description = RuntimeMSG.NOT_FOUND, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_404))),
    })
    @GetMapping("/{id}")
    public Airplane getAirplane(@PathVariable Long id) {
        return airplaneService.getAirplane(id);
    }

    @Operation(summary = "Add airplane")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = AirplaneDTO.class))),
            @ApiResponse(responseCode = "400", description = RuntimeMSG.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_400))),
    })
    @PostMapping
    public AirplaneDTO addAirplane(@RequestBody Airplane airplane) {
        return airplaneService.addAirplane(airplane);
    }

    @Operation(summary = "Delete airplane")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.SUCCESS_MESSAGE))),
            @ApiResponse(responseCode = "400", description = RuntimeMSG.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_400))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAirplane(@PathVariable Long id) {
        airplaneService.deleteAirplane(id);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
    }

}
