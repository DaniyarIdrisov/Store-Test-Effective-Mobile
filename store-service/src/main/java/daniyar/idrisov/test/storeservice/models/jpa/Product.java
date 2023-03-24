package daniyar.idrisov.test.storeservice.models.jpa;

import daniyar.idrisov.test.storeservice.models.enumerated.ProductState;
import daniyar.idrisov.test.storeservice.models.jpa.audit.Audit;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@Table(name = "product")
public class Product extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double price;

    private Long quantity;

    @ElementCollection
    @CollectionTable(name = "characteristics",
            joinColumns = {@JoinColumn(name = "product_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "head")
    @Column(name = "element")
    private Map<String, String> characteristics;

    @Enumerated(EnumType.STRING)
    private ProductState state;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "keywords", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    @Column(name="keyword")
    private List<String> keywords = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "products")
    private List<Discount> discounts = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

}
