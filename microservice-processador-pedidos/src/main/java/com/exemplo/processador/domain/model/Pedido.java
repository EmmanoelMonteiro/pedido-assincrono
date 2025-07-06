package com.exemplo.processador.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    private String id;
    private String clienteId;
    private Double valorTotal;
    private String status;
    private LocalDateTime dataCriacao;
}