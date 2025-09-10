package com.github.alexbispo.mfood.cadastro.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record AdicionaRestauranteDTO(
    @NotEmpty @NotNull String proprietario,
    @NotNull @CNPJ String cnpj,
    @Size(min = 3, max = 30) @NotNull String nome,
    LocalizacaoDTO localizacao) {}
