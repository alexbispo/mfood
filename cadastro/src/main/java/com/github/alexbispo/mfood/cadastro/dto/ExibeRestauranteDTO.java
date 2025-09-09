package com.github.alexbispo.mfood.cadastro.dto;

import java.time.LocalDateTime;

public record ExibeRestauranteDTO(
    Long id,
    String proprietario,
    String cnpj,
    String nome,
    LocalDateTime dataCriacao,
    LocalDateTime dataAtualizacao,
    LocalizacaoDTO localizacao) {}
