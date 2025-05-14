package ThyCase.ThyCaseWS.Service.Impl;

import ThyCase.ThyCaseWS.Dto.RoutesRequestDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Entity.Transportation;
import ThyCase.ThyCaseWS.Entity.Transportation.TransportType;
import ThyCase.ThyCaseWS.Mapper.TransportationMapper;
import ThyCase.ThyCaseWS.Repository.TransportationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RouteServiceImplTest {

    @Mock
    private TransportationRepository repo;

    @Mock
    private TransportationMapper mapper;

    @InjectMocks
    private RouteServiceImpl service;

    @Test
    void whenDirectFlightExists_thenReturnSingleRoute() {
        LocalDate date = LocalDate.of(2025, 5, 15);
        RoutesRequestDto req = new RoutesRequestDto();
        req.originId = 1L;
        req.destinationId = 2L;
        req.date = date;

        Transportation flight = createTransportation(100L, 1L, 2L, TransportType.FLIGHT);
        when(repo.findByOperatingDay(date.getDayOfWeek().getValue()))
                .thenReturn(List.of(flight));

        TransportationDto dto = new TransportationDto(
                100L, 1L, 2L,
                "OriginName", "DestinationName",
                flight.getType().toString(),
                "ORIGCODE",
                List.of(date.getDayOfWeek().getValue())
        );
        when(mapper.toDto(flight)).thenReturn(dto);

        List<List<TransportationDto>> routes = service.findRoutes(req);

        assertEquals(1, routes.size());
        assertEquals(List.of(dto), routes.get(0));
    }

    @Test
    void whenTransferThenFlightExists_thenReturnTransferFlightRoute() {
        LocalDate date = LocalDate.of(2025, 5, 15);
        int day = date.getDayOfWeek().getValue();
        RoutesRequestDto req = new RoutesRequestDto();
        req.originId = 10L;
        req.destinationId = 30L;
        req.date = date;

        Transportation transfer = createTransportation(200L, 10L, 20L, TransportType.BUS);
        Transportation flight  = createTransportation(300L, 20L, 30L, TransportType.FLIGHT);

        when(repo.findByOperatingDay(day))
                .thenReturn(List.of(transfer, flight));

        TransportationDto dtoTransfer = new TransportationDto(
                200L, 10L, 20L, "A", "B", transfer.getType().toString(), "CODEA", List.of(day)
        );
        TransportationDto dtoFlight = new TransportationDto(
                300L, 20L, 30L, "B", "C", flight.getType().toString(), "CODEB", List.of(day)
        );
        when(mapper.toDto(transfer)).thenReturn(dtoTransfer);
        when(mapper.toDto(flight)).thenReturn(dtoFlight);

        List<List<TransportationDto>> routes = service.findRoutes(req);

        assertEquals(1, routes.size());
        assertEquals(List.of(dtoTransfer, dtoFlight), routes.get(0));
    }

    @Test
    void whenTransferFlightTransferExists_thenReturnThreeLegRoute() {
        LocalDate date = LocalDate.of(2025, 5, 15);
        int day = date.getDayOfWeek().getValue();
        RoutesRequestDto req = new RoutesRequestDto();
        req.originId = 100L;
        req.destinationId = 400L;
        req.date = date;

        Transportation t1 = createTransportation(400L, 100L, 200L, TransportType.BUS);
        Transportation f2 = createTransportation(500L, 200L, 300L, TransportType.FLIGHT);
        Transportation t3 = createTransportation(600L, 300L, 400L, TransportType.SUBWAY);

        when(repo.findByOperatingDay(day))
                .thenReturn(List.of(t1, f2, t3));

        TransportationDto dto1 = new TransportationDto(
                400L, 100L, 200L, "X", "Y", t1.getType().toString(), "XY", List.of(day)
        );
        TransportationDto dto2 = new TransportationDto(
                500L, 200L, 300L, "Y", "Z", f2.getType().toString(), "YZ", List.of(day)
        );
        TransportationDto dto3 = new TransportationDto(
                600L, 300L, 400L, "Z", "W", t3.getType().toString(), "ZW", List.of(day)
        );
        when(mapper.toDto(t1)).thenReturn(dto1);
        when(mapper.toDto(f2)).thenReturn(dto2);
        when(mapper.toDto(t3)).thenReturn(dto3);

        List<List<TransportationDto>> routes = service.findRoutes(req);

        assertEquals(1, routes.size(), "Should find exactly one 3-leg route");
        assertEquals(List.of(dto1, dto2, dto3), routes.get(0));
    }

    private Transportation createTransportation(
            Long id, Long originId, Long destinationId, TransportType type
    ) {
        Transportation t = new Transportation();
        t.setId(id);

        Location ori = new Location();
        ori.setId(originId);
        t.setOrigin(ori);

        Location dst = new Location();
        dst.setId(destinationId);
        t.setDestination(dst);

        t.setType(type);
        return t;
    }
}
