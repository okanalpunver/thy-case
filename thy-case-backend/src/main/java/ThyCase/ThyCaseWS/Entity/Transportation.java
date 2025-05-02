package ThyCase.ThyCaseWS.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    private TransportType type;

    public enum TransportType { FLIGHT, BUS, SUBWAY, UBER }
}

