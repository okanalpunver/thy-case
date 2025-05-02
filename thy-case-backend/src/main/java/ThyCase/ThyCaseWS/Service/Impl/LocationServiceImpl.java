package ThyCase.ThyCaseWS.Service.Impl;

import ThyCase.ThyCaseWS.Dto.LocationCreateDto;
import ThyCase.ThyCaseWS.Dto.LocationDto;
import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Mapper.LocationMapper;
import ThyCase.ThyCaseWS.Repository.CityRepository;
import ThyCase.ThyCaseWS.Repository.CountryRepository;
import ThyCase.ThyCaseWS.Repository.LocationRepository;
import ThyCase.ThyCaseWS.Service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    private final LocationRepository repo;
    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;
    private final LocationMapper mapper;


    public LocationDto create(LocationCreateDto dto) {
        if (repo.existsByLocationCode(dto.locationCode))
            throw new IllegalArgumentException("Code already in use");
        Location entity = mapper.toEntity(dto, countryRepo, cityRepo);
        return mapper.toDto(repo.save(entity));
    }

    public LocationDto update(Long id, LocationCreateDto dto) {
        Location existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
        mapper.updateEntityFromDto(dto, existing, countryRepo, cityRepo);
        return mapper.toDto(repo.save(existing));
    }

    public LocationDto getById(Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));
    }

    public List<LocationDto> getAll() {
        return repo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

