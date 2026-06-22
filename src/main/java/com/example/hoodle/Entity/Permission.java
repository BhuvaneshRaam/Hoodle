package com.example.hoodle.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name ="module_id", nullable = false)
    private Module module;

    @ManyToOne
    @JoinColumn(name ="privilege_id", nullable = false)
    private Privilege privilege;
}
