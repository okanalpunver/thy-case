package ThyCase.ThyCaseWS.Controller;

import ThyCase.ThyCaseWS.Dto.RoutesRequestDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "Routes", description = "Search valid routes")
@RestController
@RequestMapping("/api/routes")
@RequiredArgsConstructor
public class RouteController {
    private final RouteService service;

    @PostMapping
    public List<List<TransportationDto>> getRoutes(
            @Valid @RequestBody RoutesRequestDto req) {
        return service.findRoutes(req);
    }
}
