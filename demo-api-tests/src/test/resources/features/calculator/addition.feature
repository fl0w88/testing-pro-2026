Feature: Addition with the calculator
  As a user of the calculator
  I want to add several numbers
  so that I do not have to calculate the sum in my head.

  Scenario: Add three numbers
    Given I have a calculator
    When I add the following numbers:
      | 1 |
      | 2 |
      | 3 |
    Then the result is 6

  Scenario: Add five numbers including decimals and negative numbers
    Given I have a calculator
    When I add the following numbers:
      | 10  |
      | 2.5 |
      | -4  |
      | 0.5 |
      | 100 |
    Then the result is 109
