package com.github.alexbispo.mfood.cadastro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExibePratoDTO(
    Long id,
    String nome,
    String descricao,
    BigDecimal preco,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao) {}
