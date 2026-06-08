package com.dev.mercadinhoastro.web.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ClienteDTO {
    private Long id;

    @NotNull(message = "Campo obrigatório!")
    @Size(min = 3, max = 100 )
    @Pattern(regexp = "[a-zA-ZÀ-ÿ ]+", message = "Nome inválido, coloque apenas Letras")
    private String nomeCompleto;

    @Size(min = 2, max = 100)
    private String apelido;

    @CPF
    @NotNull(message = "Campo obrigatório!")
    private String cpf;

    @Size(min = 1, max = 255)
    private String endereco;

    @NotNull(message = "Campo obrigatório!")
    @Pattern(regexp = "\\d+", message = "Erro, coloque somente Números")
    private String telefone;
}
