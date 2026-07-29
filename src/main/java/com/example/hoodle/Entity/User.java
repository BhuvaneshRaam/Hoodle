package com.example.hoodle.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.NumericBooleanConverter;

import java.sql.Types;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(Types.VARCHAR)
    @Column(name="user_uuid")
    private UUID userUuid;

    @Column(name="email_id",unique = true, nullable = false)
    private String emailId;

    @Column(name="user_name", nullable = false)
    private String userName;

    @Column(name = "pwd")
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="tenant_uuid", nullable = false)
    private Tenant tenant;

    @Column(name = "is_active", nullable = false)
    @ColumnDefault("1")
    @Convert(converter = NumericBooleanConverter.class)
    private boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="user_roles",
            joinColumns = @JoinColumn(name="user_uuid"),
            inverseJoinColumns = @JoinColumn(name="role_id")

    )
    private Set<Role> roles;
}
