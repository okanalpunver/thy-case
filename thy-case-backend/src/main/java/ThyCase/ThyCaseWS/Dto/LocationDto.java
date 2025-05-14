package ThyCase.ThyCaseWS.Dto;

public record LocationDto(
        Long id,
        String name,
        String countryIso2,
        Long cityId,
        String cityName,
        String locationCode
) {}

