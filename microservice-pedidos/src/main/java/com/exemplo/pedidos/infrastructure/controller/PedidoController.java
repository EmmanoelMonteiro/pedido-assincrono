package com.exemplo.pedidos.infrastructure.controller;

import com.exemplo.pedidos.application.dto.CriarPedidoDTO;
import com.exemplo.pedidos.application.service.PedidoApplicationService;
import com.exemplo.pedidos.domain.model.Pedido;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoApplicationService pedidoApplicationService;

    public PedidoController(PedidoApplicationService pedidoApplicationService) {
        this.pedidoApplicationService = pedidoApplicationService;
    }

    @PostMapping
    public ResponseEntity<Pedido> criarPedido(@RequestBody CriarPedidoDTO dto) {
        Pedido pedidoCriado = pedidoApplicationService.processarNovoPedido(dto);
        return new ResponseEntity<>(pedidoCriado, HttpStatus.ACCEPTED); // ACCEPTED pois o processamento é assíncrono
    }
}