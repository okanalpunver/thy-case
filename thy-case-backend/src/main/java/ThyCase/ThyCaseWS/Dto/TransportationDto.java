package ThyCase.ThyCaseWS.Dto;

public record TransportationDto(
        Long id,
        Long originId,
        Long destinationId,
        String type
) {}
