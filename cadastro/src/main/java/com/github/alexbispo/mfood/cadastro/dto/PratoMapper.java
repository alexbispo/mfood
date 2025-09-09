package com.github.alexbispo.mfood.cadastro.dto;

import com.github.alexbispo.mfood.cadastro.Prato;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface PratoMapper {

  Prato toEntity(AdicionaPratoDTO dto);

  ExibePratoDTO toDto(Prato prato);
}
