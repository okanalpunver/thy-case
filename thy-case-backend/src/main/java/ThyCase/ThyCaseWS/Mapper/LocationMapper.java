package ThyCase.ThyCaseWS.Mapper;

import ThyCase.ThyCaseWS.Dto.LocationCreateDto;
import ThyCase.ThyCaseWS.Dto.LocationDto;
import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Repository.CityRepository;
import ThyCase.ThyCaseWS.Repository.CountryRepository;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring", uses = { CountryMapper.class, CityMapper.class })
public interface LocationMapper {

    @Mapping(target = "countryIso2", source = "country.iso2")
    @Mapping(target = "cityId",      source = "city.id")
    @Mapping(target = "cityName",    source = "city.name")
    LocationDto toDto(Location e);

    @Mapping(target = "country",      expression = "java(countryRepo.findByIso2(dto.countryIso2).orElseThrow())")
    @Mapping(target = "city",         expression = "java(cityRepo.findByNameAndCountry(dto.cityName, countryRepo.findByIso2(dto.countryIso2).orElseThrow()).orElseThrow())")
    @Mapping(target = "name",         source = "dto.name")
    @Mapping(target = "locationCode", source = "dto.locationCode")
    Location toEntity(LocationCreateDto dto,
                      @Context CountryRepository countryRepo,
                      @Context CityRepository cityRepo);

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    @Mapping(target = "country",      expression = "java(countryRepo.findByIso2(dto.countryIso2).orElseThrow())")
    @Mapping(target = "city",         expression = "java(cityRepo.findByNameAndCountry(dto.cityName, existing.getCountry()).orElseThrow())")
    @Mapping(target = "name",         source = "dto.name")
    @Mapping(target = "locationCode", source = "dto.locationCode")
    void updateEntityFromDto(LocationCreateDto dto,
                             @MappingTarget Location existing,
                             @Context CountryRepository countryRepo,
                             @Context CityRepository cityRepo);
}

