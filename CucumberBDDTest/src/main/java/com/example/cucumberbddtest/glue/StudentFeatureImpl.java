package com.example.cucumberbddtest.glue;

import com.example.cucumberbddtest.pojo.StudentPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class StudentFeatureImpl {

    String baseUrl = "http://localhost:8080/";
    private ResponseEntity<String> response;
    private ResponseEntity<Void> deleteResponse;
    private RestTemplate restTemplate;
    private HttpHeaders jsonHeader;
    private String jsonBody;
    @Autowired
    private ObjectMapper om;

    private static final String HEADER_VALUE = "application/json";

    @Given("^Hit the request on the localhost with required endpoint -(.*) and set the json body of student with rno (.*) name (.*) address (.*)$")
    public void setRequestForPost(String endpoint, String rno, String name, String address) {
        baseUrl = baseUrl + endpoint;
        jsonHeader = new HttpHeaders();
        jsonHeader.add("Accept", HEADER_VALUE);
        jsonHeader.add("Content-Type", HEADER_VALUE);
        jsonBody = "{\"rno\":\"" + rno + "\",\"address\":\"" + address + "\",\"name\":\"" + name + "\"}";
    }

    @Given("^Hit the request on the localhost with GET endpoint -(.*)")
    public void setRequestForGet(String endpoint) {
        baseUrl = baseUrl + endpoint;
        jsonHeader = new HttpHeaders();
        jsonHeader.add("Accept", HEADER_VALUE);
        jsonHeader.add("Content-Type", HEADER_VALUE);
    }

    @Given("^Hit the request on the localhost with required endpoint -(.*) and set the header")
    public void setRequestForDelete(String endpoint) {
        baseUrl = baseUrl + endpoint;
    }


    @When("^send a POST HTTP Request to the application$")
    public void sendPostRequest() {
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, jsonHeader);
        this.restTemplate = new RestTemplate();
        response = restTemplate.postForEntity(baseUrl, entity, String.class);
    }

    @When("^send a PUT HTTP Request to the application$")
    public void sendPutRequest() {
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, jsonHeader);
        this.restTemplate = new RestTemplate();
        response = restTemplate.exchange(baseUrl, HttpMethod.PUT, entity, String.class);
    }

    @When("^send a GET HTTP Request$")
    public void sendGetRequest() {
        this.restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(baseUrl, String.class);
    }

    @When("^send a DELETE Request to delete student with rno (.*)$")
    public void sendDeleteRequest(String rno) {
        HttpEntity<?> httpEntity = new HttpEntity<>(jsonHeader);
        this.restTemplate = new RestTemplate();
        deleteResponse = restTemplate.exchange(baseUrl, HttpMethod.DELETE, httpEntity, Void.class);
    }

    @Then("^receive valid response for student with rno (.*) name (.*) address (.*)$")
    public void verifyPostResponse(String rno, String name, String address) throws JsonProcessingException {
        String responseBody = response.getBody();
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
        StudentPojo student = om.readValue(responseBody, StudentPojo.class);
        Assert.assertEquals(student.getRno(), Integer.parseInt(rno));
        Assert.assertEquals(student.getAddress(), address);
        Assert.assertEquals(student.getName(), name);
    }

    @Then("^received valid response with status code 200 OK$")
    public void verifyResponseStatus() {
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Then("^student deleted with status code 200 OK$")
    public void verifyDeleteResponseStatus() {
        Assert.assertTrue(deleteResponse.getStatusCode() == HttpStatus.OK);
    }

}

