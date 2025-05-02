package ThyCase.ThyCaseWS.Dto;

public record LocationDto(
        Long id,
        String name,
        String countryIso2,
        String cityName,
        String locationCode
) {}

