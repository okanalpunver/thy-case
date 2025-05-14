package ThyCase.ThyCaseWS.Dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class TransportationCreateDto {
    @NotNull public Long originId;
    @NotNull public Long destinationId;
    @NotNull public String type;
    @NotEmpty(message="Select at least one operating day")
    public List<@Min(1) @Max(7) Integer> operatingDays;
}
