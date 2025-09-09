package com.github.alexbispo.mfood.cadastro;

import com.github.alexbispo.mfood.cadastro.dto.AdicionaPratoDTO;
import com.github.alexbispo.mfood.cadastro.dto.AtualizaPratoDTO;
import com.github.alexbispo.mfood.cadastro.dto.ExibePratoDTO;
import com.github.alexbispo.mfood.cadastro.dto.PratoMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("restaurantes/{restauranteId}/pratos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PratoResource {

  private final PratoMapper pratoMapper;

  public PratoResource(PratoMapper pratoMapper) {
    this.pratoMapper = pratoMapper;
  }

  @GET
  public List<ExibePratoDTO> getPratosByRestaurante(
      @PathParam("restauranteId") Long restauranteId) {
    Restaurante restaurante =
        (Restaurante)
            Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

    return Prato.list("restaurante", restaurante).stream()
        .map(p -> this.pratoMapper.toDto((Prato) p))
        .toList();
  }

  @POST
  @Transactional
  public Response create(@PathParam("restauranteId") Long restauranteId, AdicionaPratoDTO dto) {
    Restaurante restaurante =
        (Restaurante)
            Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

    Prato entity = this.pratoMapper.toEntity(dto);
    entity.restaurante = restaurante;
    entity.persist();

    return Response.status(Response.Status.CREATED).build();
  }

  @PUT
  @Path("{id}")
  @Transactional
  public void update(
      @PathParam("restauranteId") Long restauranteId,
      @PathParam("id") Long id,
      AtualizaPratoDTO dto) {
    Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

    Prato prato = (Prato) Prato.findByIdOptional(id).orElseThrow(NotFoundException::new);

    prato.nome = dto.nome();
    prato.descricao = dto.descricao();
    prato.preco = dto.preco();

    prato.persist();
  }

  @DELETE
  @Path("{id}")
  @Transactional
  public void deleteById(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id) {
    Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

    Prato.findByIdOptional(id)
        .ifPresentOrElse(
            PanacheEntityBase::delete,
            () -> {
              throw new NotFoundException();
            });
  }
}
