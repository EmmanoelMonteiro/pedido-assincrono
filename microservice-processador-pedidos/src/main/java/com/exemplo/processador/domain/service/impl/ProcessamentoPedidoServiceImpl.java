package com.exemplo.processador.domain.service.impl;

import com.exemplo.processador.domain.model.Pedido;
import com.exemplo.processador.domain.service.ProcessamentoPedidoService;
import org.springframework.stereotype.Service;

@Service
public class ProcessamentoPedidoServiceImpl implements ProcessamentoPedidoService {
    @Override
    public void processar(Pedido pedido) {
        System.out.println("PROCESSANDO Pedido: " + pedido.getId());
        // Simula um processamento demorado
        try {
            Thread.sleep(5000); // 5 segundos
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        pedido.setStatus("CONCLUIDO");
        System.out.println("Pedido " + pedido.getId() + " CONCLUIDO.");
        // Aqui poderia haver lógica para atualizar um banco de dados, enviar email, etc.
    }
}