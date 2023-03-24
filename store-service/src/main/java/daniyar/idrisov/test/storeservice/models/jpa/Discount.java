package daniyar.idrisov.test.storeservice.models.jpa;

import daniyar.idrisov.test.storeservice.models.jpa.audit.Audit;
import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "discount")
public class Discount extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "discount_amount")
    private Double discountAmount;

    @Column(name = "valid_until")
    private Instant validUntil;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            schema = "store_service",
            name = "discount_products",
            joinColumns = {@JoinColumn(name = "discount_id")},
            inverseJoinColumns = {@JoinColumn(name = "product_id")}
    )
    private List<Product> products = new ArrayList<>();


}
