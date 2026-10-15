package ma.rest.spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CompteApiTest {
  @Autowired TestRestTemplate http;

  Map<String, Object> data(double solde) {
    return Map.of("solde", solde, "dateCreation", "2024-11-01", "type", "COURANT");
  }

  @Test
  void crudJsonAvecLocationEt404() {
    var created = http.postForEntity("/banque/comptes", data(1500), Map.class);
    assertEquals(201, created.getStatusCode().value());
    String url = created.getHeaders().getLocation().getPath();
    assertEquals(1500.0, ((Number) http.getForObject(url, Map.class).get("solde")).doubleValue());
    assertEquals(
        200,
        http.exchange(url, HttpMethod.PUT, new HttpEntity<>(data(2000)), String.class)
            .getStatusCode()
            .value());
    assertEquals(
        200,
        http.exchange(url, HttpMethod.DELETE, HttpEntity.EMPTY, String.class)
            .getStatusCode()
            .value());
    assertEquals(404, http.getForEntity(url, String.class).getStatusCode().value());
  }

  @Test
  void creeEtLitXml() {
    HttpHeaders h = new HttpHeaders();
    h.setContentType(MediaType.APPLICATION_XML);
    h.setAccept(java.util.List.of(MediaType.APPLICATION_XML));
    var created =
        http.postForEntity(
            "/banque/comptes",
            new HttpEntity<>(
                "<compte><solde>1234</solde><dateCreation>2024-11-01</dateCreation><type>EPARGNE</type></compte>",
                h),
            String.class);
    assertEquals(201, created.getStatusCode().value());
    assertTrue(created.getBody().contains("<solde>1234.0</solde>"));
    var read =
        http.exchange(
            created.getHeaders().getLocation().getPath(),
            HttpMethod.GET,
            new HttpEntity<>(h),
            String.class);
    assertTrue(read.getBody().contains("<type>EPARGNE</type>"));
  }

  @Test
  void valideEntrees() {
    assertEquals(
        400, http.postForEntity("/banque/comptes", data(-1), String.class).getStatusCode().value());
  }

  @Test
  void modificationEtSuppressionAbsentes() {
    assertEquals(
        404,
        http.exchange(
                "/banque/comptes/999999", HttpMethod.PUT, new HttpEntity<>(data(1)), String.class)
            .getStatusCode()
            .value());
    assertEquals(
        404,
        http.exchange("/banque/comptes/999999", HttpMethod.DELETE, HttpEntity.EMPTY, String.class)
            .getStatusCode()
            .value());
  }

  @Test
  void listeJsonEtXml() {
    assertEquals(200, http.getForEntity("/banque/comptes", String.class).getStatusCode().value());
    HttpHeaders h = new HttpHeaders();
    h.setAccept(java.util.List.of(MediaType.APPLICATION_XML));
    assertTrue(
        http.exchange("/banque/comptes", HttpMethod.GET, new HttpEntity<>(h), String.class)
            .getBody()
            .contains("<solde>"));
  }

  @Test
  void swaggerEtH2Disponibles() {
    assertEquals(200, http.getForEntity("/v3/api-docs", String.class).getStatusCode().value());
    assertEquals(200, http.getForEntity("/h2-console/", String.class).getStatusCode().value());
  }

  @Test
  void aliasPourClients() {
    assertEquals(200, http.getForEntity("/api/comptes", String.class).getStatusCode().value());
  }
}
