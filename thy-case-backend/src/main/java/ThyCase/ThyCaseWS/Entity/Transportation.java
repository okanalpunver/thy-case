package ThyCase.ThyCaseWS.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "transportation")
@NoArgsConstructor
@Getter
@Setter
public class Transportation {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id", nullable = false)
    private Location origin;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Location destination;

    @ElementCollection
    @CollectionTable(
            name = "transportation_days",
            joinColumns = @JoinColumn(name = "transportation_id")
    )
    @Column(name = "day", nullable = false)
    private List<Integer> operatingDays = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private TransportType type;

    public enum TransportType {
        FLIGHT("Flight"),
        BUS("Bus"),
        SUBWAY("Subway"),
        UBER("Uber");

        private final String label;

        TransportType(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

        public static TransportType fromString(String text) {
            for (TransportType type : TransportType.values()) {
                if (type.label.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("No TransportType with label " + text);
        }
    }

}

