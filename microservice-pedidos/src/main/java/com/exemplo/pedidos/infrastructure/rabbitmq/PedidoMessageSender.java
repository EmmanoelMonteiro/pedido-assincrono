package com.exemplo.pedidos.infrastructure.rabbitmq;

import com.exemplo.pedidos.domain.model.Pedido;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PedidoMessageSender {

    @Value("${rabbitmq.queue.pedido}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;

    // Construtor agora só injeta RabbitTemplate
    public PedidoMessageSender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean // Define a fila para o RabbitMQ
    public Queue pedidoQueue(@Value("${rabbitmq.queue.pedido}") String queueName) {
        return new Queue(queueName, true); // durable = true
    }


    public void sendNewPedido(Pedido pedido) {
        try {
            // O RabbitTemplate, uma vez configurado, fará a serialização
            rabbitTemplate.convertAndSend(queueName, pedido);
            System.out.println(" [x] Sent '" + pedido.getId() + "' to queue '" + queueName + "'");
        } catch (Exception e) {
            System.err.println("Error sending message: " + e.getMessage());
            e.printStackTrace();
        }
    }
}