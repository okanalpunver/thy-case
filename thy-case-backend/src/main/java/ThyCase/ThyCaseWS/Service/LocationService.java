package ThyCase.ThyCaseWS.Service;


import ThyCase.ThyCaseWS.Dto.LocationCreateDto;
import ThyCase.ThyCaseWS.Dto.LocationDto;

import java.util.List;

public interface LocationService {
    LocationDto create(LocationCreateDto dto);
    LocationDto update(Long id, LocationCreateDto dto);
    LocationDto getById(Long id);
    List<LocationDto> getAll();
    void delete(Long id);
}
