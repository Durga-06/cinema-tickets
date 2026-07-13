Feature: Cinema ticket purchase
  So that only valid bookings are fulfilled and charged correctly,
  the box office validates each purchase against the business rules
  before taking payment and reserving seats.

  Scenario: A family purchase is charged and seated correctly
    Given a valid account
    When they purchase 2 adult, 1 child and 1 infant tickets
    Then payment of 65 is taken
    And 3 seats are reserved

  Scenario: The maximum allowed number of tickets is accepted
    Given a valid account
    When they purchase 25 adult, 0 child and 0 infant tickets
    Then payment of 625 is taken
    And 25 seats are reserved

  Scenario: A purchase without an adult is rejected
    Given a valid account
    When they attempt to purchase 0 adult, 2 child and 0 infant tickets
    Then the purchase is rejected

  Scenario: A purchase above the maximum ticket limit is rejected
    Given a valid account
    When they attempt to purchase 26 adult, 0 child and 0 infant tickets
    Then the purchase is rejected

  Scenario: A purchase with more infants than adults is rejected
    Given a valid account
    When they attempt to purchase 1 adult, 0 child and 2 infant tickets
    Then the purchase is rejected
