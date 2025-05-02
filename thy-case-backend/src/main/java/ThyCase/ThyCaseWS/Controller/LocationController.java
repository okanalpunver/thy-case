package ThyCase.ThyCaseWS.Controller;

import ThyCase.ThyCaseWS.Dto.LocationCreateDto;
import ThyCase.ThyCaseWS.Dto.LocationDto;
import ThyCase.ThyCaseWS.Service.LocationService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Locations", description = "CRUD for locations")
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService service;

    @Operation(summary = "List all locations")
    @GetMapping
    public List<LocationDto> list() {
        return service.getAll();
    }

    @Operation(summary = "Get a location by ID")
    @GetMapping("/{id}")
    public LocationDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Create a new location")
    @ApiResponse(responseCode = "201", description = "Created",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LocationDto.class)))
    @PostMapping
    public ResponseEntity<LocationDto> create(@Valid @RequestBody LocationCreateDto dto) {
        LocationDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/locations/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Update a location")
    @PutMapping("/{id}")
    public LocationDto update(@PathVariable Long id,
                              @Valid @RequestBody LocationCreateDto dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete a location")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

