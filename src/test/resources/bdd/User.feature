Feature: UserFeature

  @integration
  Scenario: App received PUT request for User
    Given Application started
    And existing User
      | login      | password | encryptedPassword | firstName | role          |
      | userName5  | 55555    | 55555             | Vitaliy   | ADMINISTRATOR |
    # Request header defined in CommonStepDefinition
    And existing Token
      | token     | login     | dateFrom            | dateEnd             |
      | test      | userName5 | 12.09.2020 00:00:00 | 12.09.2025 00:00:00 |
    When called PUT method for '/user' with request content '/bdd/request/putUser.json'
    Then response contains status code 200