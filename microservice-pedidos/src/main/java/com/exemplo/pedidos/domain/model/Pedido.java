package com.exemplo.pedidos.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@ToString
public class Pedido {
    private String id;
    private String clienteId;
    private Double valorTotal;
    private String status; // Ex: PENDENTE, PROCESSANDO, CONCLUIDO, CANCELADO
    private LocalDateTime dataCriacao;

    public static Pedido criarNovoPedido(String clienteId, Double valorTotal) {
        return Pedido.builder()
                .id(UUID.randomUUID().toString())
                .clienteId(clienteId)
                .valorTotal(valorTotal)
                .status("PENDENTE")
                .dataCriacao(LocalDateTime.now())
                .build();
    }
}