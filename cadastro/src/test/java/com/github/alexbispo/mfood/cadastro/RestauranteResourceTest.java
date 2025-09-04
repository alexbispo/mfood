package com.github.alexbispo.mfood.cadastro;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.panache.common.Parameters;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@DBUnit(caseInsensitiveStrategy = Orthography.LOWERCASE, cacheConnection = false, schema = "public")
@QuarkusTest
@QuarkusTestResource(CadastroTestLifecycleManager.class)
@DBRider
public class RestauranteResourceTest {

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
}
