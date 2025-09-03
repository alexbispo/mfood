package com.github.alexbispo.mfood.cadastro;

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

    @GET
    public List<Prato> getPratosByRestaurante(@PathParam("restauranteId") Long restauranteId) {
        Restaurante restaurante = (Restaurante) Restaurante.findByIdOptional(restauranteId)
                .orElseThrow(NotFoundException::new);

        return Prato.list("restaurante", restaurante);
    }

    @POST
    @Transactional
    public Response create(@PathParam("restauranteId") Long restauranteId, Prato dto) {
        dto.restaurante = (Restaurante) Restaurante.findByIdOptional(restauranteId)
                .orElseThrow(NotFoundException::new);
        dto.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void update(@PathParam("restauranteId") Long restauranteId, @PathParam("id") Long id, Prato dto) {
        Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

        Prato prato = (Prato) Prato.findByIdOptional(id).orElseThrow(NotFoundException::new);

        prato.nome = dto.nome;
        prato.descricao = dto.descricao;
        prato.preco = dto.preco;

        prato.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteById(@PathParam("restauranteId") Long restauranteId,  @PathParam("id") Long id) {
        Restaurante.findByIdOptional(restauranteId).orElseThrow(NotFoundException::new);

        Prato.findByIdOptional(id).ifPresentOrElse(PanacheEntityBase::delete, () -> {
            throw new NotFoundException();
        });
    }
}
