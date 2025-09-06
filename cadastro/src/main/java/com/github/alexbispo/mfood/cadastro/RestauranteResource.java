package com.github.alexbispo.mfood.cadastro;

import com.github.alexbispo.mfood.cadastro.dto.AdicionaRestauranteDTO;
import com.github.alexbispo.mfood.cadastro.dto.ExibeRestauranteDTO;
import com.github.alexbispo.mfood.cadastro.dto.RestauranteMapper;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RestauranteResource {

    private final RestauranteMapper restauranteMapper;

    public RestauranteResource(RestauranteMapper restauranteMapper) {
        this.restauranteMapper = restauranteMapper;
    }

    @GET
    public List<ExibeRestauranteDTO> getAll() {
        return Restaurante.listAll().stream().map((r) -> {
            return this.restauranteMapper.toDto((Restaurante) r);
        }).toList();
    }

    @POST
    @Transactional
    public Response create(AdicionaRestauranteDTO dto) {
        Restaurante entity = this.restauranteMapper.toEntity(dto);
        entity.persist();
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT()
    @Path("{id}")
    @Transactional
    public void updateById(@PathParam("id") Long id, Restaurante dto) {
        Restaurante restauranteEncontrado = (Restaurante) Restaurante.findByIdOptional(id)
                .orElseThrow(NotFoundException::new);

        restauranteEncontrado.nome = dto.nome;
        restauranteEncontrado.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deleteById(@PathParam("id") Long id) {
        Restaurante.findByIdOptional(id)
                .ifPresentOrElse(PanacheEntityBase::delete, () -> {
                    throw new NotFoundException();
                });
    }
}
