package ThyCase.ThyCaseWS.Mapper;

import ThyCase.ThyCaseWS.Entity.City;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CityMapper {
    default String map(City city) {
        return city != null ? city.getName() : null;
    }
}
