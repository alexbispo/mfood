package com.github.alexbispo.mfood.cadastro.dto;

import com.github.alexbispo.mfood.cadastro.Restaurante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "jakarta")
public interface RestauranteMapper {

  //    RestauranteMapper INSTANCE = Mappers.getMapper(RestauranteMapper.class);

  Restaurante toEntity(AdicionaRestauranteDTO dto);

  ExibeRestauranteDTO toDto(Restaurante restaurante);
}
