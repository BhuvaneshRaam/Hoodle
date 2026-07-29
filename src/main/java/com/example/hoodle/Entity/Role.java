package com.example.hoodle.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.type.NumericBooleanConverter;

import java.util.Set;

@Entity
@Table(name="roles", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"role_name", "tenant_uuid"})
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, name = "role_name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_uuid")
    private Tenant tenant;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name ="role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("1")
    @Convert(converter = NumericBooleanConverter.class)
    private boolean isActive = true;
}
