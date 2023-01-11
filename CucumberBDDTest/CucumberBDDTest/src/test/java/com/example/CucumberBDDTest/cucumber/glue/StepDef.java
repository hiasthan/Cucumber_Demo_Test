package com.example.CucumberBDDTest.cucumber.glue;
import com.example.CucumberBDDTest.cucumber.Pojo.StudentPojo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class StepDef {

    String baseUrl = null;
    private ResponseEntity response;
    private ResponseEntity<Void> responseEntity;
    private RestTemplate restTemplate;
    private HttpHeaders headers;
    @Autowired
    private ObjectMapper om;

    @Given("^Hit the request on the localhost with required endpoint -(.*)")
    public void setEndpoint(String endpoint) {
        baseUrl = "http://localhost:8080/"+endpoint;
        System.out.println("Base URL :" + baseUrl);
    }

    @When("set the request header")
    public void setHeaders()
    {
        headers = new HttpHeaders();
        headers.add("Accept","application/json");
        headers.add("Content-Type","application/json");
    }

    @When ("^send a post HTTP Request to the application to add data of student with rno (.*) name (.*) address (.*)$")
    public void sendPostRequest(String rno , String name  , String address){
        String jsonBody="{\"rno\":\""+rno+"\",\"address\":\""+address+"\",\"name\":\""+name+"\"}";
        System.out.println("\n\n" + jsonBody);
        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
        this.restTemplate = new RestTemplate();
        response = restTemplate.postForEntity(baseUrl, entity, String.class);
    }

    @When ("^send a Put HTTP Request to the application to update the existing data of student with rno (.*) to name (.*) address (.*)$")
    public void sendPutRequest(String rno , String name  , String address){
        String jsonBody="{\"rno\":\""+rno+"\",\"address\":\""+address+"\",\"name\":\""+name+"\"}";
        System.out.println("\n\n" + jsonBody);
        HttpEntity<String> entity = new HttpEntity<String>(jsonBody, headers);
        this.restTemplate = new RestTemplate();
        response=restTemplate.exchange(baseUrl,HttpMethod.PUT,entity,StudentPojo.class);
    }

    @When ("^send a GET HTTP Request$")
    public void sendGetRequest() throws JsonProcessingException {
        this.restTemplate = new RestTemplate();
        response=restTemplate.getForEntity(baseUrl,String.class);
        String responseBody=response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
    }

    @When ("^send a DELETE Request to delete student with rno (.*)$")
    public void sendDeleteRequest(String rno) throws JsonProcessingException {
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        this.restTemplate = new RestTemplate();
       responseEntity=restTemplate.exchange(baseUrl,HttpMethod.DELETE,httpEntity,Void.class);
    }

    @Then ("^receive valid response for student with rno (.*) name (.*) address (.*)$")
    public  void verifyPostResponse(String rno , String name , String address) throws  JsonProcessingException{
        String responseBody = response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
        System.out.println("StatusCode --->" + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode()== HttpStatus.OK);
        StudentPojo student= om.readValue(responseBody,StudentPojo.class);
        Assert.assertEquals(student.getRno(),Integer.parseInt(rno));
        Assert.assertEquals(student.getAddress(),address);
        Assert.assertEquals(student.getName(),name);
    }

    @Then ("^received valid response with status code 200 OK$")
    public  void verifyResponseStatus(){
        String responseBody = response.getBody().toString();
        System.out.println("responseBody --->" + responseBody);
        System.out.println("StatusCode --->" + response.getStatusCode());
        Assert.assertTrue(response.getStatusCode()== HttpStatus.OK);
    }

    @Then ("^student deleted with status code 200 OK$")
    public  void verifyDeleteResponseStatus(){
        System.out.println("StatusCode --->" + responseEntity.getStatusCode());
        Assert.assertTrue(responseEntity.getStatusCode()== HttpStatus.OK);
    }

}

