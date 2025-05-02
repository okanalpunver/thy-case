package ThyCase.ThyCaseWS.Dto;

import jakarta.validation.constraints.NotNull;

public class TransportationCreateDto {
    @NotNull public Long originId;
    @NotNull public Long destinationId;
    @NotNull public String type;
}
