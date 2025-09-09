package com.github.alexbispo.mfood.cadastro.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ExibePratoDTO {

    public Long id;

    public String nome;

    public String descricao;

    public BigDecimal preco;

    public LocalDateTime dataCriacao;

    public LocalDateTime dataAtualizacao;
}
