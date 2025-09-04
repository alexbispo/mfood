package com.github.alexbispo.mfood.cadastro;

import com.github.database.rider.cdi.api.DBRider;
import com.github.database.rider.core.api.configuration.DBUnit;
import com.github.database.rider.core.api.configuration.Orthography;
import com.github.database.rider.core.api.dataset.DataSet;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.approvaltests.Approvals;
import org.approvaltests.core.Options;
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
}
