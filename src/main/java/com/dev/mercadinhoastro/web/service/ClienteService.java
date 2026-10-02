package com.dev.mercadinhoastro.web.service;

import com.dev.mercadinhoastro.web.dto.ClienteDTO;
import com.dev.mercadinhoastro.web.exceptions.ClienteNotFoundExecption;
import com.dev.mercadinhoastro.web.mapper.ClienteMapper;
import com.dev.mercadinhoastro.web.model.ClienteModel;
import com.dev.mercadinhoastro.web.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteService.class);

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public ClienteService(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    public ClienteDTO cadastrarCliente(ClienteDTO clienteDTO) {
        log.debug("Iniciando cadastro de cliente. cpf={}", clienteDTO.getCpf());

        ClienteModel clienteModel = clienteMapper.map(clienteDTO);
        ClienteModel cliente = clienteRepository.save(clienteModel);

        log.info("Cliente cadastrado com sucesso. id={}", cliente.getId());
        return clienteMapper.map(cliente);
    }

    public List<ClienteDTO> listarClientes() {
        log.debug("Listando todos os clientes");

        List<ClienteModel> clientes = clienteRepository.findAll();

        log.info("Listagem concluída. total={}", clientes.size());
        return clientes.stream()
                .map(clienteMapper::map)
                .collect(Collectors.toList());
    }

    public ClienteDTO buscarClientePorId(Long id) {
        log.debug("Buscando cliente. id={}", id);

        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente não encontrado. id={}", id);
                    return new ClienteNotFoundExecption();
                });

        log.info("Cliente encontrado. id={}", id);
        return clienteMapper.map(clienteModel);
    }

    public ClienteDTO atualizarCliente(Long id, ClienteDTO clienteDTO) {
        log.debug("Iniciando atualização de cliente. id={}", id);

        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente não encontrado para atualização. id={}", id);
                    return new ClienteNotFoundExecption();
                });

        clienteModel.setNomeCompleto(clienteDTO.getNomeCompleto());
        clienteModel.setApelido(clienteDTO.getApelido());
        clienteModel.setCpf(clienteDTO.getCpf());
        clienteModel.setEndereco(clienteDTO.getEndereco());
        clienteModel.setTelefone(clienteDTO.getTelefone());

        ClienteModel clienteAtualizado = clienteRepository.save(clienteModel);

        log.info("Cliente atualizado com sucesso. id={}", id);
        return clienteMapper.map(clienteAtualizado);
    }

    public void deletarCliente(Long id) {
        log.debug("Iniciando deleção de cliente. id={}", id);

        ClienteModel clienteModel = clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cliente não encontrado para deleção. id={}", id);
                    return new ClienteNotFoundExecption();
                });

        clienteRepository.delete(clienteModel);
        log.info("Cliente deletado com sucesso. id={}", id);
    }

}
