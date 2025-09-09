package com.github.alexbispo.mfood.cadastro.dto;

public record AdicionaRestauranteDTO(
    String proprietario, String cnpj, String nome, LocalizacaoDTO localizacao) {}
