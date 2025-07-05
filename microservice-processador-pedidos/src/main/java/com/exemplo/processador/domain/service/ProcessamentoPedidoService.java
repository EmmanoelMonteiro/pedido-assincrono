package com.exemplo.processador.domain.service;

import com.exemplo.processador.domain.model.Pedido;

public interface ProcessamentoPedidoService {
    void processar(Pedido pedido);
}