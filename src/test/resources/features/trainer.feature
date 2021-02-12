Feature: Training for Lingo
  As a player,
  I want to train my ability to make good guesses,
  in order to win lingo during the live show.

  Scenario: Guess a word
    When I start a game,
    Then the word to guess has "5" letters
      And I should see the first letter
      And my score is "0"