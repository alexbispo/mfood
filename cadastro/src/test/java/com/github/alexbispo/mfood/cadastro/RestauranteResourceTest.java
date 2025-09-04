package com.github.alexbispo.mfood.cadastro;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import junit.framework.AssertionFailedError;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static io.restassured.RestAssured.given;

@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false, schema = "public")
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
@DBRider
public class RestauranteResourceTest {

    @Inject
    EntityManager em;

    @Test
    @DataSet(value = "restaurantes-cenario-1.yml")
    public void testBuscarRestaurante() {
        String resposta = given()
                .when()
                .get("/restaurantes")
                .then().statusCode(200).extract().body().asString();

        Approvals.verify(resposta, new Options().forFile().withExtension(".json"));
    }

    @Test
    public void testCriarRestaurante() {
        Restaurante novoRstaurante = new Restaurante();
        novoRstaurante.nome = "Novo Restaurante";
        novoRstaurante.proprietario = "ID do keyclock";
        novoRstaurante.cnpj = "12345678901";

        given()
                .body(novoRstaurante)
                .contentType(ContentType.JSON)
                .when()
                .post("/restaurantes")
                .then().statusCode(201);

        var restauranteCriado = Restaurante
                .find("nome = :nome and proprietario = :proprietario and cnpj = :cnpj",
                    Parameters.with("nome", novoRstaurante.nome)
                            .and("proprietario", novoRstaurante.proprietario)
                            .and("cnpj", novoRstaurante.cnpj)).firstResult();

        Assertions.assertNotNull(restauranteCriado);
    }

    @Test
    @DataSet(value = "restaurantes-cenario-1.yml")
    public void testAtualizarRestaurante() {
        Long idRestaurante = 123L;
        String novoNome = "Restaurante Atualizado";
        var restaurante = (Restaurante) Restaurante.findByIdOptional(idRestaurante).get();
        restaurante.nome = novoNome;

        given()
                .contentType(ContentType.JSON)
                .body(restaurante)
                .when()
                .put("/restaurantes/{id}", idRestaurante)
                .then()
                .statusCode(204);

        var restauranteAtualizado = (Restaurante) Restaurante.findById(idRestaurante);
        Assertions.assertEquals(novoNome, restauranteAtualizado.nome);
    }

    @Test
    @DataSet(value = "restaurantes-cenario-1.yml")
    public void testDeletarRestaurante() {
        Long idRestaurante = 123L;

        Assertions.assertNotNull(Restaurante.findById(idRestaurante));

        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/restaurantes/{id}", idRestaurante)
                .then()
                .statusCode(204);

        // Necessário limpar o cache de primeiro nível do Hibernate (L1) e conseguir validar que o registro foi deletado.
        em.clear();

        Assertions.assertNull(Restaurante.findById(idRestaurante));
    }
}
