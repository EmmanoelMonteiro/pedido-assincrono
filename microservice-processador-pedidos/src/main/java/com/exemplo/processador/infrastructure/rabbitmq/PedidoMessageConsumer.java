package com.exemplo.processador.infrastructure.rabbitmq;

import com.exemplo.processador.application.service.ProcessadorApplicationService;
import com.exemplo.processador.domain.model.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoMessageConsumer {

    private final ProcessadorApplicationService processadorApplicationService;
    private final ObjectMapper objectMapper; // ObjectMapper será injetado

    // Injeção do ProcessadorApplicationService e do ObjectMapper
    public PedidoMessageConsumer(ProcessadorApplicationService processadorApplicationService, ObjectMapper objectMapper) {
        this.processadorApplicationService = processadorApplicationService;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = "${rabbitmq.queue.pedido}")
    public void receiveMessage(String pedidoJson) {
        try {
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);
            processadorApplicationService.receberEProcessarPedido(pedido);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}