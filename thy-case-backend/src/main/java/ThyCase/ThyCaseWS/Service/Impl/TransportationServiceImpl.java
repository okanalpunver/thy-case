package ThyCase.ThyCaseWS.Service.Impl;

import ThyCase.ThyCaseWS.Dto.TransportationCreateDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Entity.Transportation;
import ThyCase.ThyCaseWS.Mapper.TransportationMapper;
import ThyCase.ThyCaseWS.Repository.LocationRepository;
import ThyCase.ThyCaseWS.Repository.TransportationRepository;
import ThyCase.ThyCaseWS.Service.TransportationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class TransportationServiceImpl implements TransportationService {

    private final TransportationRepository repo;
    private final LocationRepository locationRepo;
    private final TransportationMapper mapper;

    @Override
    public TransportationDto create(TransportationCreateDto dto) {
        Location origin = locationRepo.findById(dto.originId)
                .orElseThrow(() -> new IllegalArgumentException("Origin not found: " + dto.originId));
        Location destination = locationRepo.findById(dto.destinationId)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found: " + dto.destinationId));

        if (origin.getId().equals(destination.getId())) {
            throw new IllegalArgumentException("Origin and destination must be different");
        }

        Optional<Transportation> existingTransportation = repo.findByOriginAndDestinationAndType(origin, destination, Transportation.TransportType.fromString(dto.type));
        Transportation entity;

        if (existingTransportation.isPresent()) {
            Transportation transportation = existingTransportation.get();
            List<Integer> toBeAddedDays = dto.operatingDays;
            List<Integer> currentDays = transportation.getOperatingDays();
            Set<Integer> uniqueDays = new LinkedHashSet<>(currentDays);
            uniqueDays.addAll(toBeAddedDays);
            transportation.setOperatingDays(new ArrayList<>(uniqueDays));
            entity = transportation;
        } else {
            entity = new Transportation();
            entity.setOrigin(origin);
            entity.setDestination(destination);
            entity.setType(Transportation.TransportType.valueOf(dto.type));
            entity.setOperatingDays(dto.operatingDays);
        }

        repo.save(entity);
        return mapper.toDto(entity);

    }

    @Override
    public TransportationDto update(Long id, TransportationCreateDto dto) {
        Transportation existing = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transportation not found: " + id));

        Location origin = locationRepo.findById(dto.originId)
                .orElseThrow(() -> new IllegalArgumentException("Origin not found: " + dto.originId));
        Location destination = locationRepo.findById(dto.destinationId)
                .orElseThrow(() -> new IllegalArgumentException("Destination not found: " + dto.destinationId));

        existing.setOrigin(origin);
        existing.setDestination(destination);
        existing.setType(Transportation.TransportType.valueOf(dto.type));
        existing.setOperatingDays(dto.operatingDays);

        return mapper.toDto(repo.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public TransportationDto getById(Long id) {
        return repo.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Transportation not found: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransportationDto> getAll() {
        return repo.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}

