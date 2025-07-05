package com.exemplo.processador.infrastructure.rabbitmq;

import com.exemplo.processador.application.service.ProcessadorApplicationService;
import com.exemplo.processador.domain.model.Pedido;
import com.fasterxml.jackson.databind.ObjectMapper;
// Importações de MessageConverter e SerializationFeature não são mais necessárias aqui
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
// import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.beans.factory.annotation.Value; // Não usado aqui
// import org.springframework.context.annotation.Bean; // Não usado aqui para o MessageConverter
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

    // REMOVA ESTE MÉTODO @Bean, se o Spring AMQP já o configura globalmente
    // ou se você o configura em uma classe dedicada de configuração do RabbitMQ.
    // public MessageConverter jsonMessageConverterConsumer() {
    //     return new Jackson2JsonMessageConverter(objectMapper);
    // }

    @RabbitListener(queues = "${rabbitmq.queue.pedido}")
    public void receiveMessage(String pedidoJson) {
        try {
            Pedido pedido = objectMapper.readValue(pedidoJson, Pedido.class);
            processadorApplicationService.receberEProcessarPedido(pedido);
        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
            e.printStackTrace(); // Adicione para depuração completa do erro
        }
    }
}