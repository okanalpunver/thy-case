package ThyCase.ThyCaseWS.Controller;

import ThyCase.ThyCaseWS.Dto.TransportationCreateDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Service.TransportationService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Transportations", description = "CRUD for transportations")
@RestController
@RequestMapping("/api/transportations")
@RequiredArgsConstructor
public class TransportationController {

    private final TransportationService service;

    @Operation(summary = "List all transportations")
    @GetMapping
    public List<TransportationDto> list() {
        return service.getAll();
    }

    @Operation(summary = "Get a transportation by ID")
    @GetMapping("/{id}")
    public TransportationDto get(@PathVariable Long id) {
        return service.getById(id);
    }

    @Operation(summary = "Create a new transportation")
    @PostMapping
    public ResponseEntity<TransportationDto> create(
            @Valid @RequestBody TransportationCreateDto dto) {
        TransportationDto created = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/transportations/" + created.id()))
                .body(created);
    }

    @Operation(summary = "Update a transportation")
    @PutMapping("/{id}")
    public TransportationDto update(@PathVariable Long id,
                                    @Valid @RequestBody TransportationCreateDto dto) {
        return service.update(id, dto);
    }

    @Operation(summary = "Delete a transportation")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

