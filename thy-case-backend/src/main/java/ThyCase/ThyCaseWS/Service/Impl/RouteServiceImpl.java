package ThyCase.ThyCaseWS.Service.Impl;


import ThyCase.ThyCaseWS.Dto.RoutesRequestDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Entity.Transportation;
import ThyCase.ThyCaseWS.Entity.Transportation.TransportType;
import ThyCase.ThyCaseWS.Mapper.TransportationMapper;
import ThyCase.ThyCaseWS.Repository.TransportationRepository;
import ThyCase.ThyCaseWS.Service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {
    private final TransportationRepository repo;
    private final TransportationMapper mapper;

    @Override
    public List<List<TransportationDto>> findRoutes(RoutesRequestDto req) {
        int day = req.date.getDayOfWeek().getValue(); // 1=Mon … 7=Sun
        Long originId = req.originId;
        Long destinationId = req.destinationId;

        // 1) Fetch all transportations operating on that day
        List<Transportation> all = repo.findByOperatingDay(day);

        // 2) Partition into flights vs transfers by origin location
        Map<Long, List<Transportation>> flightsByOrigin = new HashMap<>();
        Map<Long, List<Transportation>> transfersByOrigin = new HashMap<>();
        for (Transportation transportation : all) {
            Map<Long, List<Transportation>> map =
                    (transportation.getType() == TransportType.FLIGHT ? flightsByOrigin : transfersByOrigin);
            map.computeIfAbsent(transportation.getOrigin().getId(), k -> new ArrayList<>()).add(transportation);
        }

        List<List<TransportationDto>> routes = new ArrayList<>();

        // 3) 1-leg: direct flights originId→destinationId
        flightsByOrigin.getOrDefault(originId, List.of()).stream()
                .filter(flight -> flight.getDestination().getId().equals(destinationId))
                .forEach(flight -> routes.add(toDtoList(List.of(flight))));

        // 4) 2-leg: transfer → flight
        for (Transportation transfer : transfersByOrigin.getOrDefault(originId, List.of())) {
            Long transferDestinationId = transfer.getDestination().getId();
            flightsByOrigin.getOrDefault(transferDestinationId, List.of()).stream()
                    .filter(flight -> flight.getDestination().getId().equals(destinationId))
                    .forEach(flight -> routes.add(toDtoList(List.of(transfer, flight))));
        }

        // 5) 2-leg: flight → transfer
        for (Transportation flight : flightsByOrigin.getOrDefault(originId, List.of())) {
            Long flightDestinationId = flight.getDestination().getId();
            transfersByOrigin.getOrDefault(flightDestinationId, List.of()).stream()
                    .filter(transfer -> transfer.getDestination().getId().equals(destinationId))
                    .forEach(transfer -> routes.add(toDtoList(List.of(flight, transfer))));
        }

        // 6) 3-leg: transfer → flight → transfer
        for (Transportation transfer : transfersByOrigin.getOrDefault(originId, List.of())) {
            Long transferDestinationId = transfer.getDestination().getId();
            for (Transportation flight : flightsByOrigin.getOrDefault(transferDestinationId, List.of())) {
                Long flightDestinationId = flight.getDestination().getId();
                transfersByOrigin.getOrDefault(flightDestinationId, List.of()).stream()
                        .filter(a -> a.getDestination().getId().equals(destinationId))
                        .forEach(a -> routes.add(toDtoList(List.of(transfer, flight, a))));
            }
        }

        return routes;
    }

    private List<TransportationDto> toDtoList(List<Transportation> f) {
         return f.stream().map(mapper::toDto).toList();
    }
}