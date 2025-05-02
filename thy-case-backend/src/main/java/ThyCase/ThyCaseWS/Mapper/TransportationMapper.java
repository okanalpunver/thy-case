package ThyCase.ThyCaseWS.Mapper;

import ThyCase.ThyCaseWS.Dto.TransportationCreateDto;
import ThyCase.ThyCaseWS.Dto.TransportationDto;
import ThyCase.ThyCaseWS.Entity.Transportation;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TransportationMapper {

    @Mapping(target = "originId",      source = "origin.id")
    @Mapping(target = "destinationId", source = "destination.id")
    @Mapping(target = "type",          source = "type")
    TransportationDto toDto(Transportation entity);

    @Mapping(target = "type", source = "dto.type")
    @Mapping(target = "origin.id", source = "dto.originId")
    @Mapping(target = "destination.id", source = "dto.destinationId")
    Transportation toEntity(TransportationCreateDto dto);
}

