package com.exemplo.pedidos.domain.service.impl;

import com.exemplo.pedidos.domain.model.Pedido;
import com.exemplo.pedidos.domain.service.PedidoDomainService;
import org.springframework.stereotype.Service;

@Service
public class PedidoDomainServiceImpl implements PedidoDomainService {
    @Override
    public Pedido criarPedido(Pedido pedido) {
        // Aqui poderia haver validações de negócio, regras complexas, etc.
        // No nosso caso simples, apenas retornamos o pedido criado.
        return pedido;
    }
}