package org.smile.homeWork5;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smile.homeWork3.Location;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CityNeighborsByLocationKeyTest extends AbstractTest {

    private static final Logger logger
            = LoggerFactory.getLogger(CityNeighborsByLocationKeyTest.class);


    @Test
    void getCityNeighborsByLocationKey_shouldReturn200() throws IOException {
        logger.info("Тест код ответ 200 запущен");
        ObjectMapper mapper = new ObjectMapper();
        Location city = new Location();
        city.setLocalizedName("Novoaltaysk");

        logger.debug("Формируем мок GET /locations/v1/cities/neighbors/291662");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/neighbors/291662"))
                .willReturn(aResponse().withStatus(200)
                        .withBody(mapper.writeValueAsString(city))));

        CloseableHttpClient httpClient = HttpClients.createDefault();
        logger.debug("http-клиент создан");

        HttpGet request = new HttpGet(getBaseUrl() + "/locations/v1/cities/neighbors/291662");

        HttpResponse response = httpClient.execute(request);

        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/neighbors/291662")));
        assertEquals(200, response.getStatusLine().getStatusCode());
        assertEquals("Novoaltaysk", mapper.readValue(response
                .getEntity().getContent(), Location.class).getLocalizedName());
    }


    @Test
    void getCityNeighborsByLocationKey_shouldReturn401() throws IOException, URISyntaxException {
        logger.info("Тест код ответ 401 запущен");
        //given
        logger.debug("Формируем мок GET /locations/v1/cities/neighbors/291662");
        stubFor(get(urlPathEqualTo("/locations/v1/cities/neighbors/291662"))
                .withQueryParam("apiKey", containing("MIt4YeAng1AxFCNwKAKYiYzBqhrinwkU"))
                .willReturn(aResponse().withStatus(401).withBody("Unauthorized")));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(getBaseUrl()+"/locations/v1/cities/neighbors/291662");
        URI uri = new URIBuilder(request.getURI())
                .addParameter("apiKey", "Q_MIt4YeAng1AxFCNwKAKYiYzBqhrinwkU")
                .build();
        request.setURI(uri);
        logger.debug("http клиент создан");
        //when
        HttpResponse response = httpClient.execute(request);
        //then
        verify(getRequestedFor(urlPathEqualTo("/locations/v1/cities/neighbors/291662")));
        assertEquals(401, response.getStatusLine().getStatusCode());
        assertEquals("Unauthorized", convertResponseToString(response));
    }
}
