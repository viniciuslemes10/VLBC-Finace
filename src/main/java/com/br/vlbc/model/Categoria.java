package com.br.vlbc.model;

import com.br.vlbc.enums.Type;
import com.br.vlbc.records.CategoriaDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "tb_categoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.LAZY)
    private List<Transactions> transactions;

    public Categoria(CategoriaDTO data) {
        this.name = data.name();
        this.type = Type.valueOf(data.type());
    }
}
