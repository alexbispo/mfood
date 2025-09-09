package com.github.alexbispo.mfood.cadastro;

import static io.restassured.RestAssured.given;

import com.github.alexbispo.mfood.cadastro.dto.AdicionaRestauranteDTO;
import com.github.alexbispo.mfood.cadastro.dto.AtualizaRestauranteDTO;
import com.github.alexbispo.mfood.cadastro.dto.LocalizacaoDTO;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@DBUnit(
    caseInsensitiveStrategy = Orthography.LOWERCASE,
    cacheConnection = false,
    schema = "public",
    alwaysCleanBefore = true)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
@DBRider
public class RestauranteResourceTest {

  @Inject EntityManager em;

  @Test
  //    @DataSet(value = {"restaurantes-cenario-1.yml", "pratos-cenario-1.yml"}, tableOrdering =
  // {"restaurante", "prato"})

  @DataSet(
      value = {"restaurantes-cenario-2.yml", "localizacao-cenario-1.yml"},
      tableOrdering = {"localizacao", "restaurante"})
  public void testBuscarRestaurante() {
    String resposta =
        given().when().get("/restaurantes").then().statusCode(200).extract().body().asString();

    Approvals.verify(resposta, new Options().forFile().withExtension(".json"));
  }

  @Test
  public void testCriarRestaurante() {
    LocalizacaoDTO localizacao = new LocalizacaoDTO(-23.6491, -46.8524);

    AdicionaRestauranteDTO novoRstaurante =
        new AdicionaRestauranteDTO(
            "Novo Restaurante", "ID do keyclock", "12345678901", localizacao);

    given()
        .body(novoRstaurante)
        .contentType(ContentType.JSON)
        .when()
        .post("/restaurantes")
        .then()
        .statusCode(201);

    Restaurante restauranteCriado =
        Restaurante.find(
                "nome = :nome and proprietario = :proprietario and cnpj = :cnpj",
                Parameters.with("nome", novoRstaurante.nome())
                    .and("proprietario", novoRstaurante.proprietario())
                    .and("cnpj", novoRstaurante.cnpj()))
            .firstResult();

    Assertions.assertNotNull(restauranteCriado);
    Assertions.assertEquals(localizacao.latitude(), restauranteCriado.localizacao.latitude);
    Assertions.assertEquals(localizacao.longitude(), restauranteCriado.localizacao.longitude);
  }

  @Test
  @DataSet(value = "restaurantes-cenario-1.yml")
  public void testAtualizarRestaurante() {
    Long idRestaurante = 123L;
    String novoNome = "Restaurante Atualizado - " + LocalDateTime.now();
    var restaurante = new AtualizaRestauranteDTO(novoNome);

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

    // Necessário limpar o cache de primeiro nível do Hibernate (L1) e conseguir validar que o
    // registro foi deletado.
    em.clear();

    Assertions.assertNull(Restaurante.findById(idRestaurante));
  }
}
