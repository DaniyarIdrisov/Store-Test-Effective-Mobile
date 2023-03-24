package daniyar.idrisov.test.securityservice.models.jpa;

import daniyar.idrisov.test.securityservice.models.enumerated.Role;
import daniyar.idrisov.test.securityservice.models.enumerated.UserState;
import daniyar.idrisov.test.securityservice.models.jpa.audit.Audit;
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
@Table(name = "user")
public class User extends Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    @Column(name = "hash_password")
    private String hashPassword;

    private Double balance;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private UserState state;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Notification> notifications = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "createdBy", orphanRemoval = true)
    private List<Notification> createdNotifications = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "createdBy", orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Purchase> purchases = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "createdBy", orphanRemoval = true)
    private List<Organization> organizations = new ArrayList<>();

    public boolean isNonBanned() {
        return !this.state.equals(UserState.FROZEN);
    }

    public boolean isConfirmed() {
        return this.state.equals(UserState.ACTIVE);
    }


}
