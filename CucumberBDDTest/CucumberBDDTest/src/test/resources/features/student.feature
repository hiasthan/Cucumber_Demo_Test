Feature: Student feature
  Scenario: Add the student record
    Given Hit the request on the localhost with required endpoint -addStu
    When  set the request header
    And send a post HTTP Request to the application to add data of student with rno 6 name BVF address ZZZ
    Then receive valid response for student with rno 6 name BVF address ZZZ

    Scenario: Update the student record
      Given Hit the request on the localhost with required endpoint -upStu/3
      When set the request header
      And send a Put HTTP Request to the application to update the existing data of student with rno 3 to name DDG address ZZZ
      Then received valid response with status code 200 OK

      Scenario: Get student record
        Given Hit the request on the localhost with required endpoint -getDetails
        When send a GET HTTP Request
        Then received valid response with status code 200 OK

        Scenario: Delete the student Record
          Given Hit the request on the localhost with required endpoint -deleteStu/1
          When set the request header
          And send a DELETE Request to delete student with rno 1
          Then student deleted with status code 200 OK
