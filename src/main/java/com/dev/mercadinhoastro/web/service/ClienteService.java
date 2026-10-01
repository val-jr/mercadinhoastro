package com.dev.mercadinhoastro.web.service;

import com.dev.mercadinhoastro.web.dto.ClienteDTO;
import com.dev.mercadinhoastro.web.mapper.ClienteMapper;
import com.dev.mercadinhoastro.web.model.ClienteModel;
import com.dev.mercadinhoastro.web.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteDTO cadastrarCliente(ClienteDTO clienteDTO){
        ClienteModel clienteModel = clienteMapper.map(clienteDTO);
        ClienteModel cliente = clienteRepository.save(clienteModel);

        return clienteMapper.map(cliente);
    }

    public List<ClienteDTO> listarClientes(){

        List<ClienteModel> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(clienteMapper::map)
                .collect(Collectors.toList());
    }
}
