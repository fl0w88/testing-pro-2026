# Copyright (c) 2026 Nagarro GmbH. Author: Florin Balint.
# SPDX-License-Identifier: Apache-2.0

Feature: PENNY start page
  As a visitor of the PENNY online shop
  I want the start page to load with a cookie prompt
  so that I can decide about cookies before I use the website.

  Scenario: The start page loads with cookie prompt and title
    Given I want to verify the PENNY website
    When I call the startpage
    Then the cookie prompt is loaded
    And the page has a title

  Scenario: Accepting all cookies closes the cookie prompt
    Given I want to verify the PENNY website
    When I call the startpage
    And I accept all cookies
    Then the cookie prompt is closed
    And the page title contains "PENNY"
