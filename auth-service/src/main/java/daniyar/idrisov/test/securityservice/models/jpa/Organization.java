package daniyar.idrisov.test.securityservice.models.jpa;

import daniyar.idrisov.test.securityservice.models.jpa.audit.Audit;
import daniyar.idrisov.test.securityservice.models.enumerated.OrganizationState;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "organization")
public class Organization extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private OrganizationState state;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "organization", orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

}
