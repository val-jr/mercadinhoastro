package com.dev.mercadinhoastro.web.mapper;

import com.dev.mercadinhoastro.web.dto.ClienteDTO;
import com.dev.mercadinhoastro.web.model.ClienteModel;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public ClienteModel map(ClienteDTO clienteDTO){
        ClienteModel clienteModel = new ClienteModel();
        clienteModel.setId(clienteDTO.getId());
        clienteModel.setApelido(clienteDTO.getApelido());
        clienteModel.setCpf(clienteDTO.getCpf());
        clienteModel.setEndereco(clienteDTO.getEndereco());
        clienteModel.setTelefone(clienteDTO.getTelefone());
        clienteModel.setNomeCompleto(clienteDTO.getNomeCompleto());

        return clienteModel;
    }

    public ClienteDTO map(ClienteModel clienteModel){
        ClienteDTO clienteDTO = new ClienteDTO();
        clienteDTO.setId(clienteModel.getId());
        clienteDTO.setApelido(clienteModel.getApelido());
        clienteDTO.setCpf(clienteModel.getCpf());
        clienteDTO.setEndereco(clienteModel.getEndereco());
        clienteDTO.setTelefone(clienteModel.getTelefone());
        clienteDTO.setNomeCompleto(clienteModel.getNomeCompleto());

        return clienteDTO;
    }
}
