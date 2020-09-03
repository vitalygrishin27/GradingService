Feature: GetConfigurationFeature

  @integration
  Scenario: App received GET request for Configuration
    Given Application started
    And existing Configuration
      | configKey | configValue |
      | theme     | yellow      |
    When called GET method for '/configuration'
    Then response contains status code 200
    And response contains Configuration
      | configKey | configValue |
      | theme     | yellow      |

  @integration
  Scenario: App received POST request for existing Configuration
    Given Application started
    And existing Configuration
      | configKey | configValue |
      | theme     | yellow      |
    When called POST method for '/configuration' with request content '/bdd/request/postConfig.json'
    Then response contains status code 200
    And assert that Configuration is
      | configKey | configValue |
      | theme     | green       |

  @integration
  Scenario: App received POST request for one existing and one new Configuration
    Given Application started
    And existing Configuration
      | configKey | configValue |
      | theme     | yellow      |
    When called POST method for '/configuration' with request content '/bdd/request/postNewConfig.json'
    Then response contains status code 200
    And assert that Configuration is
      | configKey | configValue |
      | theme     | green       |
      | font      | Arial       |

  @integration
  Scenario: App received DELETE request for one existing Configuration
    Given Application started
    And existing Configuration
      | configKey | configValue |
      | theme     | green       |
      | font      | Arial       |
    When called DELETE method for '/configuration/theme'
    Then response contains status code 200
    And assert that Configuration is
      | configKey | configValue |
      | font      | Arial       |
    And assert that Configuration not exists
      | configKey |
      | theme     |
