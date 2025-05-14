package ThyCase.ThyCaseWS.Mapper;

import ThyCase.ThyCaseWS.Dto.TransportationCreateDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Entity.Location;
import ThyCase.ThyCaseWS.Entity.Transportation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransportationMapper {

    @Mapping(target="originId", source="origin.id")
    @Mapping(target="destinationId", source="destination.id")
    @Mapping(target="originName",    expression="java(formatLocation(entity.getOrigin()))")
    @Mapping(target="destinationName", expression="java(formatLocation(entity.getDestination()))")
    @Mapping(target="type", source="type")
    @Mapping(target="operatingDays", source="operatingDays")
    @Mapping(target = "originCode", source = "origin.locationCode")
    TransportationDto toDto(Transportation entity);

    @Mapping(target="type", source="dto.type")
    @Mapping(target="origin.id",      source="dto.originId")
    @Mapping(target="destination.id", source="dto.destinationId")
    @Mapping(target="operatingDays",  source="operatingDays")
    Transportation toEntity(TransportationCreateDto dto);

    default String formatLocation(Location loc) {
        if (loc == null || loc.getCity() == null || loc.getCountry() == null) return "";
        return loc.getName() + " " + loc.getCity().getName() + "/" + loc.getCountry().getIso2();
    }
}
