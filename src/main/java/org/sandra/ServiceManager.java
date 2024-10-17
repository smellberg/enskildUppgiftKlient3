package org.sandra;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.HttpEntities;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.sandra.Models.Movies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ServiceManager <T> {

    static CloseableHttpClient httpClient = HttpClients.createDefault();
    static ObjectMapper mapper = new ObjectMapper();

    // För GET-request med enskilt objekt
    public T sendGetRequest(String uri, Class<T> responseType) throws IOException, ParseException {
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            if (response.getCode() == 404) {
                System.out.println("Filmen finns inte.");
            } else {
                System.out.println(String.format("Något har gått fel! Statuskod: %d", response.getCode()));
            }
            return null;
        }

        HttpEntity entity = response.getEntity();
        String jsonResp = EntityUtils.toString(entity);

        return mapper.readValue(jsonResp, responseType);
    }

    // För GET-request med lista av objekt
    public List<T> sendGetRequestList(String uri, TypeReference<List<T>> typeReference) throws IOException, ParseException {
        HttpGet request = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println(String.format("Något har gått fel! Statuskod: %d", response.getCode()));
        }

        HttpEntity entity = response.getEntity();
        String jsonResp = EntityUtils.toString(entity);

        return mapper.readValue(jsonResp, typeReference);
    }

    // För POST-request
    public T sendPostRequest(String uri, T requestBody, Class<T> responseType) throws IOException, ParseException {
        HttpPost request = new HttpPost(uri);
        StringEntity jsonPayload = new StringEntity(mapper.writeValueAsString(requestBody), ContentType.APPLICATION_JSON);
        request.setEntity(jsonPayload);

        CloseableHttpResponse response = httpClient.execute(request);

        int statusCode = response.getCode();
        if (statusCode != 200) {
            if (statusCode == 404) {
                System.out.println("Författaren finns inte.");
            } else if (statusCode == 500) {
                System.out.println("Ett fel har inträffat på servern. Försök igen senare.");
            } else {
                System.out.println(String.format("Något har gått fel! Statuskod: %d", statusCode));
            }
            return null;
        }

        HttpEntity entity = response.getEntity();
        String jsonResp = EntityUtils.toString(entity);

        return mapper.readValue(jsonResp, responseType);
    }

    // För PUT-request
    public T sendPutRequest(String uri, T requestBody, Class<T> responseType) throws IOException, ParseException {
        HttpPut request = new HttpPut(uri);
        StringEntity jsonPayload = new StringEntity(mapper.writeValueAsString(requestBody), ContentType.APPLICATION_JSON);
        request.setEntity(jsonPayload);

        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() != 200) {
            System.out.println(String.format("Något har gått fel! Statuskod: %d", response.getCode()));
        }

        HttpEntity entity = response.getEntity();
        String jsonResp = EntityUtils.toString(entity);

        return mapper.readValue(jsonResp, responseType);
    }
    //För DELETE-request
    public void sendDeleteRequest(String uri) throws IOException, ParseException{
        HttpDelete request = new HttpDelete(uri);
        CloseableHttpResponse response = httpClient.execute(request);

        if (response.getCode() !=200){
            System.out.println(String.format("Något gick fel, statuskod: %d", response.getCode()));
        }
    }
}
