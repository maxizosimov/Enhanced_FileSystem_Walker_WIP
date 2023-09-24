package cs3500.pa02;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * test class containing tests for the Question class
 */
public class QuestionTest {

  @Test
  public void testGetQuestionText() {
    String[] questionData = {"Question 1", "Answer 1", "Difficulty: Easy"};
    Question question = new Question(questionData);

    String expectedQuestionText = "Question 1";
    String actualQuestionText = question.getQuestionText();

    assertEquals(expectedQuestionText, actualQuestionText);
  }

  @Test
  public void testGetDifficulty() {
    String[] questionData = {"Question 1", "Answer 1", "Difficulty: Easy"};
    Question question = new Question(questionData);

    String expectedDifficulty = "Difficulty: Easy";
    String actualDifficulty = question.getDifficulty();

    assertEquals(expectedDifficulty, actualDifficulty);
  }

  @Test
  public void testSetDifficulty() {
    String[] questionData = {"Question 1", "Answer 1", "Difficulty: Easy"};
    Question question = new Question(questionData);

    String newDifficulty = "Difficulty: Hard";
    question.setDifficulty(newDifficulty);

    String expectedDifficulty = "Difficulty: Hard";
    String actualDifficulty = question.getDifficulty();

    assertEquals(expectedDifficulty, actualDifficulty);
  }

  @Test
  public void testGetAnswer() {
    String[] questionData = {"Question 1", "Answer 1", "Difficulty: Easy"};
    Question question = new Question(questionData);

    String expectedAnswer = "Answer 1";
    String actualAnswer = question.getAnswer();

    assertEquals(expectedAnswer, actualAnswer);
  }
}