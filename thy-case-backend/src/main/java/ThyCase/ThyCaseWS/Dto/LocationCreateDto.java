package ThyCase.ThyCaseWS.Dto;

import jakarta.validation.constraints.*;

public class LocationCreateDto {
    @NotBlank @Size(max = 100)
    public String name;

    @NotBlank @Size(min = 2, max = 2)
    public String countryIso2;

    @NotBlank @Size(max = 100)
    public String cityName;

    @NotBlank @Pattern(regexp = "^[A-Z0-9]{3,5}$")
    public String locationCode;
}
