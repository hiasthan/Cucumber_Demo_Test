package com.example.CucumberBDDTest.glue;
import com.example.CucumberBDDTest.Pojo.StudentPojo;
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
    private ResponseEntity response;
    private ResponseEntity<Void> deleteResponse;
    private RestTemplate restTemplate;
    private HttpHeaders jsonHeader;

    private String jsonBody;
    @Autowired
    private ObjectMapper om;

    @Given("^Hit the request on the localhost with required endpoint -(.*) and set the json body of student with rno (.*) name (.*) address (.*)$")
    public void setRequestForPost(String endpoint, String rno, String name, String address) {
        baseUrl = baseUrl + endpoint;
        System.out.println("Base URL :" + baseUrl);
        jsonHeader = new HttpHeaders();
        jsonHeader.add("Accept", "application/json");
        jsonHeader.add("Content-Type", "application/json");
        jsonBody = "{\"rno\":\"" + rno + "\",\"address\":\"" + address + "\",\"name\":\"" + name + "\"}";
    }

    @Given("^Hit the request on the localhost with GET endpoint -(.*)")
    public void setRequestForGet(String endpoint) {
        baseUrl = baseUrl + endpoint;
        System.out.println("Base URL :" + baseUrl);
        jsonHeader = new HttpHeaders();
        jsonHeader.add("Accept", "application/json");
        jsonHeader.add("Content-Type", "application/json");
    }

    @Given("^Hit the request on the localhost with required endpoint -(.*) and set the header")
    public void setRequestForDelete(String endpoint) {
        baseUrl = baseUrl + endpoint;
        System.out.println("Base URL :" + baseUrl);
    }


    @When("^send a POST HTTP Request to the application$")
    public void sendPostRequest() {
        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, jsonHeader);
        this.restTemplate = new RestTemplate();
        System.out.println("Endpoint :" + baseUrl);
        System.out.println("\n Json Body --> " + jsonBody);
        response = restTemplate.postForEntity(baseUrl, entity, String.class);
    }

    @When("^send a PUT HTTP Request to the application$")
    public void sendPutRequest() {
        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, jsonHeader);
        this.restTemplate = new RestTemplate();
        response = restTemplate.exchange(baseUrl, HttpMethod.PUT, entity, StudentPojo.class);
    }

    @When("^send a GET HTTP Request$")
    public void sendGetRequest() throws JsonProcessingException {
        this.restTemplate = new RestTemplate();
        response = restTemplate.getForEntity(baseUrl, String.class);
        String responseBody = response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
    }

    @When("^send a DELETE Request to delete student with rno (.*)$")
    public void sendDeleteRequest(String rno) throws JsonProcessingException {
        HttpEntity<?> httpEntity = new HttpEntity<>(jsonHeader);
        this.restTemplate = new RestTemplate();
        deleteResponse = restTemplate.exchange(baseUrl, HttpMethod.DELETE, httpEntity, Void.class);
    }

    @Then("^receive valid response for student with rno (.*) name (.*) address (.*)$")
    public void verifyPostResponse(String rno, String name, String address) throws JsonProcessingException {
        String responseBody = response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
        System.out.println("StatusCode --->" + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
        StudentPojo student = om.readValue(responseBody, StudentPojo.class);
        Assert.assertEquals(student.getRno(), Integer.parseInt(rno));
        Assert.assertEquals(student.getAddress(), address);
        Assert.assertEquals(student.getName(), name);
    }

    @Then("^received valid response with status code 200 OK$")
    public void verifyResponseStatus() {
        String responseBody = response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
        System.out.println("StatusCode --->" + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode() == HttpStatus.OK);
    }

    @Then("^student deleted with status code 200 OK$")
    public void verifyDeleteResponseStatus() {
        System.out.println("StatusCode --->" + deleteResponse.getStatusCode());
        Assert.assertTrue(deleteResponse.getStatusCode() == HttpStatus.OK);
    }

}

