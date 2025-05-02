package ThyCase.ThyCaseWS.Mapper;

import ThyCase.ThyCaseWS.Entity.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    default String map(Country country) {
        return country != null ? country.getIso2() : null;
    }
}