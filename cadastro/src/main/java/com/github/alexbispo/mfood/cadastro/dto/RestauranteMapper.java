package com.github.alexbispo.mfood.cadastro.dto;

import com.github.alexbispo.mfood.cadastro.Restaurante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface RestauranteMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCriacao", ignore = true)
  @Mapping(target = "dataAtualizacao", ignore = true)
  @Mapping(target = "localizacao.id", ignore = true)
  @Mapping(target = "localizacao.dataCriacao", ignore = true)
  @Mapping(target = "localizacao.dataAtualizacao", ignore = true)
  Restaurante adicionaToEntity(AdicionaRestauranteDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "proprietario", ignore = true)
  @Mapping(target = "cnpj", ignore = true)
  @Mapping(target = "dataCriacao", ignore = true)
  @Mapping(target = "dataAtualizacao", ignore = true)
  @Mapping(target = "localizacao.id", ignore = true)
  @Mapping(target = "localizacao.dataCriacao", ignore = true)
  @Mapping(target = "localizacao.dataAtualizacao", ignore = true)
  Restaurante atualizaToEntity(AtualizaRestauranteDTO dto, @MappingTarget Restaurante restaurante);

  @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "dataAtualizacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  ExibeRestauranteDTO toDto(Restaurante restaurante);
}
