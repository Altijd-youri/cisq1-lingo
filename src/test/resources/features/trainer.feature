Feature: Training for Lingo
  As a player,
  I want to train my ability to make good guesses,
  in order to win lingo during the live show.

  Scenario: Start a new Game
    When I start a game,
    Then the word to guess has "5" letters
      And I should see the first letter
      And my score is "0"

  Scenario Outline: Start a new round
    Given I am playing a game
    And the round was won
    And the last word has "<previous length>" letters
    When I start a new round
    Then the word to guess has "<next length> letters

    Examples:
      | previous length | next length |
      | 5               | 6           |
      | 6               | 7           |
      | 7               | 5           |