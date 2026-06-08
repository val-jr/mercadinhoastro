package com.dev.mercadinhoastro.web.controller;

import com.dev.mercadinhoastro.web.dto.ClienteDTO;
import com.dev.mercadinhoastro.web.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cliente")
@Validated
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<ClienteDTO> cadastrarCliente(@RequestBody @Valid ClienteDTO cliente){
        ClienteDTO clienteDTO = clienteService.cadastrarCliente(cliente);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteDTO);
    }
}
