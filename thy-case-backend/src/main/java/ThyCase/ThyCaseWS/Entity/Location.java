package ThyCase.ThyCaseWS.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "location", uniqueConstraints = {
        @UniqueConstraint(columnNames = "location_code")
})
@Getter
@Setter
@NoArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String name;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "city_id", nullable = false)
    private City city;

    @Column(name = "location_code", nullable = false, length = 5, unique = true)
    @NotBlank
    @Pattern(regexp = "^[A-Z0-9]{3,5}$",
            message = "Code must be 3–5 uppercase letters or digits")
    private String locationCode;

}
