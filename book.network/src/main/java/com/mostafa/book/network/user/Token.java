package com.mostafa.book.network.user;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Token {

    @Id
    @GeneratedValue()
    private Integer id;

    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiredAt;
    private LocalDateTime validateAt;

    @ManyToOne
    @JoinColumn(name = "userId",nullable = false)
    private User user;
}
