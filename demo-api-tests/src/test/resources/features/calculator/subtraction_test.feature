Feature: Subtraction with the calculator

  Scenario: Subtract 4 - 3 = 1
    Given I have a calculator
    When I subtract 4 and 3
    Then the result is 1

  Scenario Outline: Subtract <nr1> - <nr2>
    Given I have a calculator
    When I subtract <nr1> and <nr2>
    Then the result is <result>

    Examples:
      | nr1 | nr2 | result |
      | 5   | 3   | 2      |
      | 10  | 4   | 6      |
      | 7   | 2   | 5      |
