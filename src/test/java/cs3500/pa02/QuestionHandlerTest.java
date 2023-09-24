package cs3500.pa02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * test class to test the QuestionHandler class and its methods
 */
public class QuestionHandlerTest {

  private QuestionHandler questionHandler;
  private final String questionPath = "path/to/questions.sr";
  private final int questionAmount = 2;

  /**
   * Setting up for testing generate questions specifically. Could not figure out a general setup
   * that worked for all methods in the QuestionHandler class
   *
   * @throws IOException throws correctly if an I/O error occurs
   */
  @BeforeEach
  public void setup() throws IOException {
    // Prepare a temporary question bank file
    Path tempQuestionBank = Files.createTempFile("questionBank", ".sr");
    Files.write(tempQuestionBank, List.of(
        "[[Question 1:::Answer 1]] Difficulty: Hard",
        "[[Question 2:::Answer 2]] Difficulty: Easy",
        "[[Question 3:::Answer 3]] Difficulty: Hard"
    ));

    // Create the QuestionHandler with the temporary question bank path
    questionHandler = new QuestionHandler(
        tempQuestionBank.toString(), questionAmount, new Scanner(System.in));
  }

  @Test
  public void testGenerateQuestions() throws IOException {
    // Prepare the standard input for selecting the answer (not necessary if this is not tested)
    System.setIn(new ByteArrayInputStream("1\n1\n".getBytes()));

    // Redirect the standard output to a byte array
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    questionHandler.generateQuestions();

    // Check if the questions were displayed correctly
    assertFalse(outContent.toString().contains("Answer 1"));
  }

  /**
   * setup for tests
   */
  @BeforeEach
  public void setup2() {
    questionHandler = new QuestionHandler(questionPath, questionAmount, new Scanner(System.in));
  }

  /**
   * tests for the main constructor (not artificial one with scanner) in QuestionHandler
   */
  @Test
  public void testMainConstructor() {
    QuestionHandler questionHandler = new QuestionHandler(questionPath, questionAmount);

    assertEquals(questionPath, questionHandler.questionPath);
    assertEquals(questionAmount, questionHandler.questionAmount);
  }

  /**
   * tests to check that the, with MOCK INPUT, correct questions are displayed
   */
  @Test
  public void testDisplayQuestions() {
    // Prepare a mock question list
    ArrayList<Question> questions = new ArrayList<>();
    Question question1 = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Easy"});
    Question question2 = new Question(new String[] {"Question 2", "Answer 2", "Difficulty: Hard"});
    questions.add(question1);
    questions.add(question2);

    // Prepare a mock input for user responses
    String mockInput = "1\n3\n2\n4\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);

    // Use System.out to capture the output for testing
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    // Call the method to display questions
    questionHandler.displayQuestions(questions, new Scanner(System.in));

    // Verify the captured output
    String output = outputStream.toString();
    assertTrue(output.contains("Question 1"));
    assertTrue(output.contains("Answer 2"));
  }

  /**
   * tests to check for the out print of stats correctly
   */
  @Test
  public void testDisplayStats() {

    // Use System.out to capture the output for testing
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    questionHandler.displayStats();
    String output = outputStream.toString();

    assertTrue(output.contains("Total questions answered: "));
    assertTrue(output.contains("Total questions that changed from easy to hard: "));
    assertTrue(output.contains("Total questions that changed from hard to easy: "));
    assertTrue(output.contains("Updated total number of hard questions: "));
    assertTrue(output.contains("Updated total number of easy questions: "));
  }

  // CHECKING THE SEPARATE BRANCHES OF SWITCH STATEMENT IN QUESTIONHANDLER

  /**
   * Checking case 1
   */
  @Test
  public void testDisplayQuestions_Case1() {
    ArrayList<Question> questions = new ArrayList<>();
    Question question = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Hard"});
    questions.add(question);
    String mockInput = "1\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    questionHandler.displayQuestions(questions, new Scanner(System.in));

    assertEquals("Difficulty: Easy", question.getDifficulty());
    assertEquals(1, questionHandler.getHardToEasy());
    assertEquals(1, questionHandler.getTotalEasy());
    assertEquals(1, questionHandler.getTotalAnswered());
    assertEquals(0, questionHandler.getTotalHard());
  }

  /**
   * Checking case 2
   */
  @Test
  public void testDisplayQuestions_Case2() {
    ArrayList<Question> questions = new ArrayList<>();
    Question question = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Easy"});
    questions.add(question);
    String mockInput = "2\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    questionHandler.displayQuestions(questions, new Scanner(System.in));

    assertEquals("Difficulty: Hard", question.getDifficulty());
    assertEquals(1, questionHandler.getEasyToHard());
    assertEquals(0, questionHandler.getTotalEasy());
    assertEquals(1, questionHandler.getTotalAnswered());
    assertEquals(1, questionHandler.getTotalHard());
  }

  /**
   * Checking case 3
   */
  @Test
  public void testDisplayQuestions_Case3() {
    ArrayList<Question> questions = new ArrayList<>();
    Question question = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Easy"});
    questions.add(question);
    String mockInput = "3\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));
    questionHandler.displayQuestions(questions, new Scanner(System.in));

    String output = outputStream.toString();
    assertTrue(output.contains("Answer 1"));
    assertEquals(1, questionHandler.totalAnswered);
  }

  /**
   * Checking case 4
   */
  @Test
  public void testDisplayQuestions_Case4() {
    ArrayList<Question> questions = new ArrayList<>();
    Question question1 = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Easy"});
    Question question2 = new Question(new String[] {"Question 2", "Answer 2", "Difficulty: Hard"});
    questions.add(question1);
    questions.add(question2);

    String mockInput = "4\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStream));

    questionHandler.displayQuestions(questions, new Scanner(System.in));

    String output = outputStream.toString();
    assertTrue(output.contains("Question 1"));
    assertFalse(output.contains("Question 2"));
  }

  /**
   * If user gives invalid response (anything but 1,2,3,4)
   */
  @Test
  public void testDisplayQuestions_InvalidResponse() {
    // Prepare a mock question list
    ArrayList<Question> questions = new ArrayList<>();
    Question question = new Question(new String[] {"Question 1", "Answer 1", "Difficulty: Easy"});
    questions.add(question);

    // Prepare a mock input for user responses
    String mockInput = "invalid\n";
    InputStream inputStream = new ByteArrayInputStream(mockInput.getBytes());
    System.setIn(inputStream);

    // Call the method to display questions and assert that an IllegalArgumentException is thrown
    assertThrows(IllegalArgumentException.class, () ->
        questionHandler.displayQuestions(questions, new Scanner(System.in)));
  }
}