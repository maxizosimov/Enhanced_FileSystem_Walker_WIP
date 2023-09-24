package cs3500.pa02;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * test class containing tests for the Driver class
 */
public class DriverTest {

  // Constants resembling temporary directories or paths for testing
  private static final String TEST_DIRECTORY = "testDir";
  private static final String OUTPUT_DIRECTORY = "outputDir";
  private static final String STUDY_GUIDE_PATH = OUTPUT_DIRECTORY + "/studyGuide.md";
  private static final String QUESTION_BANK_PATH = TEST_DIRECTORY + "/questionBank.sr";

  // Create a temporary directory in order to test the main method
  @TempDir
  Path tempDir;

  /**
   * tests the case if an invalid directory is supplied (branch) in the cmd args
   */
  @Test
  public void testStudyGuidesWithInvalidDir() {
    String invalidDirectory = tempDir.resolve("nonExistentDirectory").toString();
    String outputFilePath = tempDir.resolve("studyGuide.md").toString();
    String[] input = {invalidDirectory, "filename", outputFilePath};

    assertThrows(IOException.class, () -> Driver.createStudyGuides(input));
  }

  /**
   * tests the case where the ordering flag in the cmdline arguments is not acceptable
   *
   * @throws IOException throws exception if we cant access the directory
   */
  @Test
  public void testStudyGuidesWithInvalidOrderFlag() throws IOException {
    String validDirectory = Files.createDirectory(tempDir.resolve("validDirectory")).toString();
    String outputFilePath = tempDir.resolve("studyGuide.md").toString();
    String[] input = {validDirectory, "invalidOrderingFlag", outputFilePath};

    assertThrows(IllegalArgumentException.class, () -> Driver.createStudyGuides(input));
  }

  /**
   * tests the case where main is supplied with cmd args --> to go into studyGuide creation mode
   *
   * @throws IOException if some I/O error occurs while accessing args or creating temp files
   */
  @Test
  public void testMain_WithArgs() throws IOException {
    String validDirectory = Files.createDirectory(tempDir.resolve("validDirectory")).toString();
    String outputFilePath = tempDir.resolve("studyGuide.md").toString();
    String[] args = {validDirectory, "filename", outputFilePath};

    assertDoesNotThrow(() -> Driver.main(args));
  }

  /**
   * tests the case where an invalid directory is given as first cmd arg and throws accordingly
   */
  @Test
  public void testCreateStudyGuides_InvalidDirectory() {
    String[] input = {"invalidDirectory", "filename", STUDY_GUIDE_PATH};

    assertThrows(FileNotFoundException.class, () -> Driver.createStudyGuides(input));
  }

  /**
   * if a invalid path to the .sr file with the questions are supplied
   */
  @Test
  public void testCreateStudySession_InvalidQuestionBankPath() {
    String invalidPath = "invalidQuestionBankPath";
    String questionCount = "5";
    System.setIn(new ByteArrayInputStream((invalidPath + "\n" + questionCount).getBytes()));
    assertThrows(NoSuchElementException.class, () -> Driver.createStudySession());
  }

  /**
   * If there is a negative question count we test here
   */
  @Test
  public void testCreateStudySession_InvalidQuestionCount() {
    String invalidQuestionCount = "invalid";
    System.setIn(new
        ByteArrayInputStream((QUESTION_BANK_PATH + "\n" + invalidQuestionCount).getBytes()));
    assertThrows(NoSuchElementException.class, () -> Driver.createStudySession());
  }
}