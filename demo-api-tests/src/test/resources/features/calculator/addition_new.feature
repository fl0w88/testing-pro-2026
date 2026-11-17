Feature: Addition with the calculator

  Scenario: Add 4 + 3 = 7 numbers
    Given I have a calculator
    When I add the following numbers:
      | 4 |
      | 3 |
    Then the result is 7