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
        int day = req.date.getDayOfWeek().getValue();
        Long originId = req.originId;
        Long destinationId = req.destinationId;

        List<Transportation> all = repo.findByOperatingDay(day);

        Map<Long, List<Transportation>> flightsByOrigin = new HashMap<>();
        Map<Long, List<Transportation>> transfersByOrigin = new HashMap<>();
        for (Transportation transportation : all) {
            Map<Long, List<Transportation>> map =
                    (transportation.getType() == TransportType.FLIGHT ? flightsByOrigin : transfersByOrigin);
            map.computeIfAbsent(transportation.getOrigin().getId(), k -> new ArrayList<>()).add(transportation);
        }

        List<List<TransportationDto>> routes = new ArrayList<>();

        // direct flights
        flightsByOrigin.getOrDefault(originId, List.of()).stream()
                .filter(flight -> flight.getDestination().getId().equals(destinationId))
                .forEach(flight -> routes.add(toDtoList(List.of(flight))));

        // transfer → flight
        for (Transportation transfer : transfersByOrigin.getOrDefault(originId, List.of())) {
            Long transferDestinationId = transfer.getDestination().getId();
            flightsByOrigin.getOrDefault(transferDestinationId, List.of()).stream()
                    .filter(flight -> flight.getDestination().getId().equals(destinationId))
                    .forEach(flight -> routes.add(toDtoList(List.of(transfer, flight))));
        }

        // flight → transfer
        for (Transportation flight : flightsByOrigin.getOrDefault(originId, List.of())) {
            Long flightDestinationId = flight.getDestination().getId();
            transfersByOrigin.getOrDefault(flightDestinationId, List.of()).stream()
                    .filter(transfer -> transfer.getDestination().getId().equals(destinationId))
                    .forEach(transfer -> routes.add(toDtoList(List.of(flight, transfer))));
        }

        // transfer → flight → transfer
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