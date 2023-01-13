Feature: Student feature
  Scenario: Add the student record
    Given Hit the request on the localhost with required endpoint -addStu and set the json body of student with rno 6 name BVF address ZZZ
    When send a POST HTTP Request to the application
    Then receive valid response for student with rno 6 name BVF address ZZZ

  Scenario: Update the student record
    Given Hit the request on the localhost with required endpoint -upStu/3 and set the json body of student with rno 3 name DDG address TTT
    When send a PUT HTTP Request to the application
    Then received valid response with status code 200 OK

  Scenario: Get student record
    Given Hit the request on the localhost with GET endpoint -getDetails
    When send a GET HTTP Request
    Then received valid response with status code 200 OK

  Scenario: Delete the student Record
    Given Hit the request on the localhost with required endpoint -deleteStu/1 and set the header
    When send a DELETE Request to delete student with rno 1
    Then student deleted with status code 200 OK