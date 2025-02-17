package com.mostafa.book.network.role;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mostafa.book.network.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Role {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false,unique = true)
    private String name;

    @Column(nullable = false,updatable = false)
    @CreatedDate
    private LocalDate createdAt;

    @Column(insertable = false)
    @LastModifiedDate
    private LocalDate lastUpdatedAt;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users;
}
