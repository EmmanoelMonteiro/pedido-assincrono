package com.exemplo.pedidos.application.service;

import com.exemplo.pedidos.application.dto.CriarPedidoDTO;
import com.exemplo.pedidos.domain.model.Pedido;
import com.exemplo.pedidos.domain.service.PedidoDomainService;
import com.exemplo.pedidos.infrastructure.rabbitmq.PedidoMessageSender;
import org.springframework.stereotype.Service;

@Service
public class PedidoApplicationService {

    private final PedidoDomainService pedidoDomainService;
    private final PedidoMessageSender pedidoMessageSender;

    public PedidoApplicationService(PedidoDomainService pedidoDomainService, PedidoMessageSender pedidoMessageSender) {
        this.pedidoDomainService = pedidoDomainService;
        this.pedidoMessageSender = pedidoMessageSender;
    }

    public Pedido processarNovoPedido(CriarPedidoDTO dto) {
        // 1. Cria o objeto de domínio Pedido
        Pedido novoPedido = Pedido.criarNovoPedido(dto.getClienteId(), dto.getValorTotal());

        // 2. Executa lógica de domínio (se houver)
        Pedido pedidoPersistido = pedidoDomainService.criarPedido(novoPedido); // Simula persistência

        // 3. Envia a mensagem para a fila (desacoplamento)
        pedidoMessageSender.sendNewPedido(pedidoPersistido);

        return pedidoPersistido;
    }
}