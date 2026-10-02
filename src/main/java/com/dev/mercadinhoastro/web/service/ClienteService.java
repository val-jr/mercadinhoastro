package com.dev.mercadinhoastro.web.service;

import com.dev.mercadinhoastro.web.dto.ClienteDTO;
import com.dev.mercadinhoastro.web.exceptions.ClienteNotFoundExecption;
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

    public ClienteDTO cadastrarCliente(ClienteDTO clienteDTO) {
        ClienteModel clienteModel = clienteMapper.map(clienteDTO);
        ClienteModel cliente = clienteRepository.save(clienteModel);

        return clienteMapper.map(cliente);
    }

    public List<ClienteDTO> listarClientes() {

        List<ClienteModel> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(clienteMapper::map)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Long id) {
        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    return new ClienteNotFoundExecption();
                });
        return clienteMapper.map(clienteModel);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundExecption::new);

        clienteModel.setNomeCompleto(clienteDTO.getNomeCompleto());
        clienteModel.setApelido(clienteDTO.getApelido());
        clienteModel.setCpf(clienteDTO.getCpf());
        clienteModel.setEndereco(clienteDTO.getEndereco());
        clienteModel.setTelefone(clienteDTO.getTelefone());

        ClienteModel clienteAtualizado = clienteRepository.save(clienteModel);
        return clienteMapper.map(clienteAtualizado);
    }

    public void deletarCliente(Long id) {
        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(ClienteNotFoundExecption::new);

        clienteRepository.delete(clienteModel);
    }

}
