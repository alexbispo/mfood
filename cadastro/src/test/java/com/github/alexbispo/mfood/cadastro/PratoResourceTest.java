package com.github.alexbispo.mfood.cadastro;

import com.github.alexbispo.mfood.cadastro.dto.AdicionaPratoDTO;
import com.github.alexbispo.mfood.cadastro.dto.AtualizaPratoDTO;
import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
public class PratoResourceTest {

  @Inject EntityManager em;

  @Test
  @DataSet(
      value = {"restaurantes-cenario-1.yml", "pratos-cenario-1.yml"},
      tableOrdering = {"restaurante", "prato"})
  public void testObterPratosPorRestaurante() {
    Long idRestaurante = 123L;

    String resposta =
        RestAssured.given()
            .when()
            .get("/restaurantes/{id}/pratos", idRestaurante)
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString();

    Approvals.verify(resposta, new Options().forFile().withExtension(".json"));
  }

  @Test
  @DataSet(value = "restaurantes-cenario-1.yml")
  public void testCriarPrato() {
    Restaurante restaurante = Restaurante.findById(123L);

    AdicionaPratoDTO novoPrato = new AdicionaPratoDTO();
    novoPrato.nome = "Novo Prato testCriarPrato";
    novoPrato.preco = new BigDecimal("25.00");

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(novoPrato)
        .when()
        .post("/restaurantes/{id}/pratos", restaurante.id)
        .then()
        .statusCode(201);

    Prato pratoCriado = Prato.find("nome", novoPrato.nome).firstResult();
    Assertions.assertNotNull(pratoCriado);
    Assertions.assertEquals(novoPrato.preco, pratoCriado.preco);
    Assertions.assertEquals(restaurante.id, pratoCriado.restaurante.id);
  }

  @Test
  @DataSet(
      value = {"restaurantes-cenario-1.yml", "pratos-cenario-1.yml"},
      tableOrdering = {"restaurante", "prato"})
  public void testAtualizarPrato() {
    Long idPrato = 123L;
    Prato pratoParaAtualizar = Prato.findById(idPrato);

    AtualizaPratoDTO pratoDTO = new AtualizaPratoDTO();
    pratoDTO.nome = "Feijoada";
    pratoDTO.descricao = "Feijoada deliciosa";
    pratoDTO.preco = new BigDecimal("49.90");

    RestAssured.given()
        .contentType(ContentType.JSON)
        .body(pratoDTO)
        .when()
        .put(
            "/restaurantes/{id}/pratos/{idPrato}",
            pratoParaAtualizar.restaurante.id,
            pratoParaAtualizar.id)
        .then()
        .statusCode(204);

    em.clear();

    Prato pratoAtualizado = Prato.findById(idPrato);
    Assertions.assertEquals(pratoDTO.nome, pratoAtualizado.nome);
    Assertions.assertEquals(pratoDTO.descricao, pratoAtualizado.descricao);
    Assertions.assertEquals(pratoDTO.preco, pratoAtualizado.preco);
  }

  @Test
  @DataSet(
      value = {"restaurantes-cenario-1.yml", "pratos-cenario-1.yml"},
      tableOrdering = {"restaurante", "prato"})
  public void testDeletarPrato() {
    Long idPrato = 123L;
    Prato pratoParaDeletar = Prato.findById(idPrato);

    RestAssured.given()
        .contentType(ContentType.JSON)
        .when()
        .delete(
            "/restaurantes/{idRestaurante}/pratos/{idPrato}",
            pratoParaDeletar.restaurante.id,
            pratoParaDeletar.id)
        .then()
        .statusCode(204);

    em.clear();

    Prato pratoDeletado = Prato.findById(idPrato);
    Assertions.assertNull(pratoDeletado);
  }
}
