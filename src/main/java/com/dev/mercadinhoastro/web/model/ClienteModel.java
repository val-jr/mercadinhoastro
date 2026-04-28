package com.dev.mercadinhoastro.web.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_cliente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false, name = "nome_completo")
    private String nomeCompleto;

    @Column(length = 100, name = "apelido")
    private String apelido;

    @Column(length = 11, nullable = false, unique = true, name = "cpf")
    private String cpf;

    @Column(length = 255, name = "endereco")
    private String endereco;

    @Column(length = 20, nullable = false, name = "telefone")
    private String telefone;
}
