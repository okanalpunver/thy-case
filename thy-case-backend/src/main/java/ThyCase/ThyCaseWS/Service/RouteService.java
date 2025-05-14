package ThyCase.ThyCaseWS.Service;

import ThyCase.ThyCaseWS.Dto.RoutesRequestDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import java.util.List;

public interface RouteService {
    List<List<TransportationDto>> findRoutes(RoutesRequestDto request);
}
