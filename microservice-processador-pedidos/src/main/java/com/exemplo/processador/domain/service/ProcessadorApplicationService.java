package com.exemplo.processador.application.service;

import com.exemplo.processador.domain.model.Pedido;
import com.exemplo.processador.domain.service.ProcessamentoPedidoService;
import org.springframework.stereotype.Service;

@Service
public class ProcessadorApplicationService {

    private final ProcessamentoPedidoService processamentoPedidoService;

    public ProcessadorApplicationService(ProcessamentoPedidoService processamentoPedidoService) {
        this.processamentoPedidoService = processamentoPedidoService;
    }

    public void receberEProcessarPedido(Pedido pedido) {
        System.out.println("Recebido para processamento: " + pedido.toString());
        processamentoPedidoService.processar(pedido);
    }
}