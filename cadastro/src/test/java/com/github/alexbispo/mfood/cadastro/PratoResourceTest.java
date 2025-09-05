package com.github.alexbispo.mfood.cadastro;

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
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false, schema = "public", alwaysCleanBefore=true)
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
@DBRider
public class PratoResourceTest {

    @Inject
    EntityManager em;

    @Test
    @DataSet(value = {"restaurantes-cenario-1.yml", "pratos-cenario-1.yml"}, tableOrdering = {"restaurante", "prato"})
    public void testObterPratosPorRestaurante() {
        Long idRestaurante = 123L;

        String resposta = RestAssured
                .given()
                .when()
                .get("/restaurantes/{id}/pratos", idRestaurante)
                .then().statusCode(200).extract().body().asString();

        Approvals.verify(resposta, new Options().forFile().withExtension(".json"));
    }

    @Test
    @DataSet(value = "restaurantes-cenario-1.yml")
    public void testCriarPrato() {
        Restaurante restaurante = Restaurante.findById(123L);

        Prato novoPrato = new Prato();
        novoPrato.restaurante = restaurante;
        novoPrato.nome = "Novo Prato testCriarPrato";
        novoPrato.preco = new BigDecimal("25.0");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(novoPrato)
                .when()
                .post("/restaurantes/{id}/pratos", restaurante.id)
                .then()
                .statusCode(201);

        Prato pratoCriado = Prato.find("nome", novoPrato.nome).firstResult();
        Assertions.assertNotNull(pratoCriado);
    }
}
