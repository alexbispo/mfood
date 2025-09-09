package com.github.alexbispo.mfood.cadastro.dto;

import java.time.LocalDateTime;

public class ExibeRestauranteDTO {

  public Long id;

  public String proprietario;

  public String cnpj;

  public String nome;

  public LocalDateTime dataCriacao;

  public LocalDateTime dataAtualizacao;

  public LocalizacaoDTO localizacao;
}
