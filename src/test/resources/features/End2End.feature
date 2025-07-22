@E2E_API
Feature: End to End Test for ToolsQA's Book Story API
  Description: The purpose of these tests are to cover end to end happy flows for customer

  Book Store Swagger URL: https://bookstore.toolsqa.com/swagger/index.html

  Background: User generates token authorization
    Given I am an authorized user

  Scenario Outline: Authorized user is able to And and Remove a book
    Given A list of books is available
    When I "<action>" a book to my reading list
    Then The books is "<actionResult>"
    Examples:
      | action | actionResult |
      | add    | added        |
      | remove | removed      |

