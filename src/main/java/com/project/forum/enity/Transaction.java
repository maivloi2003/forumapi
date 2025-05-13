package com.project.forum.enity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.forum.enums.StatusPayment;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "transaction")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    String id;

    BigDecimal amount;

    String currency;

    String message;

    @CreatedDate
    @Column(updatable = false)
    LocalDateTime created_at;

    String status;

    String payment_method;

    String transaction_id;

    String payable_id;

    String payable_type;

    String code;

    @Column(length = 2000)
    String url_payment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    Users users;

}
