package ThyCase.ThyCaseWS.Dto;

import java.util.List;

public record TransportationDto(
        Long id,
        Long originId,
        Long destinationId,
        String originName,
        String destinationName,
        String type,
        String originCode,
        List<Integer> operatingDays
) {}
