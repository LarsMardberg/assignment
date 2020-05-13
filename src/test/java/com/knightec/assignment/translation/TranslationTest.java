package com.knightec.assignment.translation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TranslationTest {

    @LocalServerPort
    private int port;
    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @Autowired
    TranslationRepository repository;

    @Before
    public void setup(){
    }

    @Test
    public void translationDoNotExistInDatabaseShouldReturnNOT_FOUND() {
        String languageCode = "se";
        String from = "fun";
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<TranslationDTO> response = restTemplate.exchange(
                createURLWithPort("/api/v1/translation?from=" + from + "&languageCode=" + languageCode), HttpMethod.GET, entity, TranslationDTO.class);
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void translationExistInDatabaseShouldReturnCorrectItem() {
        String languageCode = "se";
        String from = "key";
        String to = "ord";
        Translation translation = new Translation(languageCode,from,to);
        repository.save(translation);
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<TranslationDTO> response = restTemplate.exchange(
                createURLWithPort("/api/v1/translation?from=" + from + "&languageCode=" + languageCode), HttpMethod.GET, entity, TranslationDTO.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        TranslationDTO result = response.getBody();
        assertEquals(languageCode,result.getLanguageCode());
        assertEquals(from,result.getFrom());
        assertEquals(to,result.getTo());
        repository.deleteAll();
    }


    @Test
    public void storingNonExistingkeyShouldReturnOKAndExistInDatabase() {
        String languageCode = "se";
        String from = "key";
        String to = "ord";
        TranslationDTO translation = new TranslationDTO(languageCode,from,to);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/api/v1/translation?from=" + from + "&languageCode=" + languageCode),translation,
                void.class);
        assertTrue(HttpStatus.OK.equals(response.getStatusCode()));
        Optional<Translation> queryResult = repository.findByLanguageCodeAndFrom("se","key");
        Translation result = queryResult.get();
        assertEquals(languageCode,result.getLanguageCode());
        assertEquals(from,result.getFrom());
        assertEquals(to,result.getTo());
        repository.deleteAll();
    }

    @Test
    public void storingDuplicatesShouldReturnBadRequest() {
        String languageCode = "se";
        String key = "key";
        String value = "ord";
        TranslationDTO translation = new TranslationDTO(languageCode,key,value);
        ResponseEntity<Void> response = restTemplate.postForEntity(createURLWithPort("/api/v1/translation?key=" + key + "&languageCode=" + languageCode),translation,
                void.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        ResponseEntity<Void> response2 = restTemplate.postForEntity(createURLWithPort("/api/v1/translation?key=" + key + "&languageCode=" + languageCode),translation,
                void.class);
        assertEquals(HttpStatus.BAD_REQUEST,response2.getStatusCode());
        repository.deleteAll();
    }

    @Test
    public void languageCodeStoredWithSmallCharacters() {
        String languageCodeLarge = "SE";
        String languageCode = "se";
        String from = "key";
        String to = "ord";
        TranslationDTO translation = new TranslationDTO(languageCode,from,to);
        restTemplate.postForEntity(createURLWithPort("/api/v1/translation?from=" + from + "&languageCode=" + languageCodeLarge),translation,
                void.class);
        Optional<Translation> result = repository.findByLanguageCodeAndFrom(languageCode,from);
        assertEquals(languageCode,result.get().getLanguageCode());
        repository.deleteAll();
    }


    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

}