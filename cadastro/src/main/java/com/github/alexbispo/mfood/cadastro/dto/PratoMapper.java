package com.github.alexbispo.mfood.cadastro.dto;

import com.github.alexbispo.mfood.cadastro.Prato;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "jakarta")
public interface PratoMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCriacao", ignore = true)
  @Mapping(target = "dataAtualizacao", ignore = true)
  @Mapping(target = "restaurante", ignore = true)
  Prato adicionaToEntity(AdicionaPratoDTO dto);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "dataCriacao", ignore = true)
  @Mapping(target = "dataAtualizacao", ignore = true)
  @Mapping(target = "restaurante", ignore = true)
  Prato atualizaToEntity(AtualizaPratoDTO dto, @MappingTarget Prato prato);

  @Mapping(target = "dataCriacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  @Mapping(target = "dataAtualizacao", dateFormat = "dd/MM/yyyy HH:mm:ss")
  ExibePratoDTO toDto(Prato prato);
}
