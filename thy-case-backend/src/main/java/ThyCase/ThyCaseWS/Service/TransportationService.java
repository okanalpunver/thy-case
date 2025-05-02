package ThyCase.ThyCaseWS.Service;

import ThyCase.ThyCaseWS.Dto.TransportationCreateDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;

import java.util.List;

public interface TransportationService {
    TransportationDto create(TransportationCreateDto dto);
    TransportationDto update(Long id, TransportationCreateDto dto);
    TransportationDto getById(Long id);
    List<TransportationDto> getAll();
    void delete(Long id);
}
