package com.exemplo.pedidos.infrastructure.rabbitmq;

import com.exemplo.pedidos.domain.model.Pedido;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

// Não precisa mais importar ObjectMapper ou suas classes de suporte aqui
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.SerializationFeature;
// import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
// import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
// import org.springframework.amqp.support.converter.MessageConverter;

@Component
public class PedidoMessageSender {

    @Value("${rabbitmq.queue.pedido}")
    private String queueName;

    private final RabbitTemplate rabbitTemplate;
    // Remova a declaração do ObjectMapper aqui:
    // private final ObjectMapper objectMapper;

    // Construtor agora só injeta RabbitTemplate
    public PedidoMessageSender(RabbitTemplate rabbitTemplate) { // Remova 'ObjectMapper objectMapper' do parâmetro
        this.rabbitTemplate = rabbitTemplate;
        // Remova a inicialização do ObjectMapper aqui:
        // this.objectMapper = objectMapper;
    }

    @Bean // Define a fila para o RabbitMQ
    public Queue pedidoQueue(@Value("${rabbitmq.queue.pedido}") String queueName) {
        return new Queue(queueName, true); // durable = true
    }

    // Este método foi removido no erro anterior, e está correto, ele não deve estar aqui.
    // public MessageConverter jsonMessageConverter() { ... }

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