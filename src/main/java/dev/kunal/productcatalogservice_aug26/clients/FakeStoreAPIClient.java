package dev.kunal.productcatalogservice_aug26.clients;

import dev.kunal.productcatalogservice_aug26.dtos.FakeStoreProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class FakeStoreAPIClient {

    @Autowired
    @Qualifier("externalRestTemplate")
    private RestTemplate restTemplate;

    public ResponseEntity<FakeStoreProductDto> getProductDetailsById(Long id) {
        return restTemplate.getForEntity("https://fakestoreapi.com/products/{id}",
                FakeStoreProductDto.class,
                id
        );
    }

    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request,
                                               Class<T> responseType, Object... uriVariables) throws RestClientException {

        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, HttpMethod.POST, requestCallback, responseExtractor, uriVariables);
    }


    public <T> ResponseEntity<T> requestForEntity(String url, HttpMethod httpMethod, @Nullable Object request,
                                                  Class<T> responseType, Object... uriVariables) throws RestClientException {

        // RestTemplate restTemplate = restTemplateBuilder.build();
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request, responseType);
        ResponseExtractor<ResponseEntity<T>> responseExtractor = restTemplate.responseEntityExtractor(responseType);
        return restTemplate.execute(url, httpMethod, requestCallback, responseExtractor, uriVariables);
    }

    public void delete(String url, Object... uriVariables) throws RestClientException {
        restTemplate.execute(url, HttpMethod.DELETE, null, null, uriVariables);
    }


    // Validate response

    public Boolean validateResponse(ResponseEntity<FakeStoreProductDto> responseEntity) {
        if (responseEntity.getBody() != null && responseEntity.getStatusCode().equals(HttpStatusCode.valueOf(200))) {
            return true;
        }
        return false;

    }

}
