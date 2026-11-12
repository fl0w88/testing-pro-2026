Feature: Addition of two numbers (scenario outline)
  A scenario outline is a template: the scenario runs once for every row
  of the "Examples" table. The placeholders <a>, <b> and <sum> are replaced
  by the values of the current row.

  Scenario Outline: <a> + <b> = <sum>
    Given I have a calculator
    When I add <a> and <b>
    Then the result is <sum>

    Examples:
      | a    | b   | sum  |
      | 1    | 2   | 3    |
      | 0    | 0   | 0    |
      | -5   | 5   | 0    |
      | -3   | -4  | -7   |
      | 2.5  | 0.5 | 3    |
      | 1000 | 1   | 1001 |
