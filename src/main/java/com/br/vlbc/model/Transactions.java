package com.br.vlbc.model;

import com.br.vlbc.enums.Method;
import com.br.vlbc.enums.Type;
import com.br.vlbc.records.TransactionsDTO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private Method method;

    @Column(name = "value", nullable = false)
    private BigDecimal value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id")
    private Category category;

    public Transactions(TransactionsDTO data, Category category, User user) {
        this.name = data.name();
        this.dateOfCreation = LocalDateTime.now();
        this.updateDate = dateOfCreation;
        this.type = data.type();
        this.method = data.method();
        this.value = data.value();
        this.user = user;
        this.category = category;
    }
}
