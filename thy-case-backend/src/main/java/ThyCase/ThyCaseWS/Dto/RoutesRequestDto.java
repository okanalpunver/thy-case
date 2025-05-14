package ThyCase.ThyCaseWS.Dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class RoutesRequestDto {
    @NotNull public Long originId;
    @NotNull public Long destinationId;
    @NotNull @FutureOrPresent(message = "Date must be today or in the future") public LocalDate date;
}