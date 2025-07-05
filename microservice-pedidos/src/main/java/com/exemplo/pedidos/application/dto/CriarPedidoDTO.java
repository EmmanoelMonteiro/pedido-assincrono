package com.exemplo.pedidos.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CriarPedidoDTO {
    private String clienteId;
    private Double valorTotal;
}