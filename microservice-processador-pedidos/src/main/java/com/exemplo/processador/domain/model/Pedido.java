package com.exemplo.processador.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor // Adicione esta anotação
@AllArgsConstructor // Adicione esta anotação para que @Builder funcione corretamente
public class Pedido {
    private String id;
    private String clienteId;
    private Double valorTotal;
    private String status;
    private LocalDateTime dataCriacao;
}