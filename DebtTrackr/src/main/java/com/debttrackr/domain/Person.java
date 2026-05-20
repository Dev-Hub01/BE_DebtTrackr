package com.debttrackr.domain;

import com.debttrackr.domain.enumeration.Relation;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Email(message = "Valid email required")
    @NotBlank(message = "Email is required")
    @Column(nullable = false)
    private String email;

    /** Phone / WhatsApp contact number */
    private String contactNumber;

    private String address;

    private String city;

    private String state;

    private String country;

    /** How this person is related to the owner */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relation relation;

    /** Optional notes about this person */
    private String notes;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Boolean active;

}
