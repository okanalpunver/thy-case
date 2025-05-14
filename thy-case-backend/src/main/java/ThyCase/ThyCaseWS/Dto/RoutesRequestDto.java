package ThyCase.ThyCaseWS.Dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RoutesRequestDto {
    @NotNull public Long originId;
    @NotNull public Long destinationId;
    @NotNull public LocalDate date;
}