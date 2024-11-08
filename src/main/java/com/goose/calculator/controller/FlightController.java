package com.goose.calculator.controller;

import com.goose.calculator.config.RuntimeMSG;
import com.goose.calculator.dto.FlightDTO;
import com.goose.calculator.model.Flight;
import com.goose.calculator.model.WayPoint;
import com.goose.calculator.service.FlightService;
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

@Tag(name = "FLights")
@RestController
@RequestMapping("airplanes/{id}/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @Operation(summary = "Fetch all airplane flights")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = FlightDTO[].class))),
    })
    @GetMapping
    public List<FlightDTO> getFlights(@PathVariable Long id) {
        return flightService.getFlights(id);
    }

    @Operation(summary = "Fetch airplane flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = Flight.class))),
            @ApiResponse(responseCode = "404", description = RuntimeMSG.NOT_FOUND, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_404))),
    })
    @GetMapping("/{flightId}")
    public Flight getFlight(@PathVariable Long id, @PathVariable Long flightId) {
        return flightService.getFlight(id, flightId);
    }

    @Operation(summary = "Calculate flight for plane")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = FlightDTO.class))),
            @ApiResponse(responseCode = "400", description = RuntimeMSG.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_400))),
            @ApiResponse(responseCode = "500", description = RuntimeMSG.INTERNAL_ERROR, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_500))),

    })
    @PostMapping("/calculate")
    public FlightDTO calculateFlight(@PathVariable Long id, @RequestBody List<WayPoint> wayPoints) throws Exception {
        return flightService.calculateFlight(id, wayPoints);
    }

    @Operation(summary = "Delete flight")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = RuntimeMSG.SUCCESS, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.SUCCESS_MESSAGE))),
            @ApiResponse(responseCode = "400", description = RuntimeMSG.BAD_REQUEST, content = @Content(schema = @Schema(implementation = ResponseEntity.class), examples = @ExampleObject(RuntimeMSG.ERROR_400))),
    })
    @DeleteMapping("/{flightId}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long id, @PathVariable Long flightId) {
        flightService.deleteFlight(id, flightId);
        return ResponseEntity.status(200).contentType(MediaType.APPLICATION_JSON).body("ok");
    }
}
