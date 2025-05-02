package ThyCase.ThyCaseWS.CommandLineRunner;

import ThyCase.ThyCaseWS.Entity.City;
import ThyCase.ThyCaseWS.Entity.Country;
import ThyCase.ThyCaseWS.Repository.CityRepository;
import ThyCase.ThyCaseWS.Repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class GeoDataLoader implements CommandLineRunner {

    @Value("classpath:db/data/countries.csv")
    private Resource countriesCsv;

    @Value("classpath:db/data/states.csv")
    private Resource statesCsv;

    private final CountryRepository countryRepo;
    private final CityRepository cityRepo;


    @Override
    public void run(String... args) throws Exception {
        /* try (Reader reader = new InputStreamReader(countriesCsv.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {
            for (CSVRecord rec : parser) {
                String iso2 = rec.get("iso2");
                String name = rec.get("name");
                countryRepo.findByIso2(iso2)
                        .orElseGet(() -> countryRepo.save(new Country(iso2, name)));
            }
        }

        try (Reader reader = new InputStreamReader(statesCsv.getInputStream(), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .parse(reader)) {
            for (CSVRecord rec : parser) {
                String iso2 = rec.get("country_code"); // header name in states.csv
                String stateName = rec.get("name");
                countryRepo.findByIso2(iso2).ifPresent(country -> {
                    City city = new City();
                    city.setName(stateName);
                    city.setCountry(country);
                    cityRepo.save(city);
                });
            }
        } */
    }
}

