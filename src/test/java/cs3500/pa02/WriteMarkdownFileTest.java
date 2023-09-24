package cs3500.pa02;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * test class to test WriteMarkdownFile class and its methods
 */
class WriteMarkdownFileTest {

  private WriteMarkdownFile wmf;

  // Creating temp directory for tests
  @TempDir
  Path tempDir;

  /**
   * Sets up temp files uses directory to complete the paths using resolve as well as adding
   * contents to the files in order to compare files in testing
   *
   * @throws IOException in case an I/O error occurs or the parent directory does not exist
   */
  @BeforeEach
  void setUp() throws IOException {
    // Create temporary files with some content
    Path file1 = Files.createFile(tempDir.resolve("arrays.md"));
    Files.writeString(file1, """
        # This is arrays.md content
        [[Some content]]
        Some non important text
        :::Some question:::
        """);
    Path file2 = Files.createFile(tempDir.resolve("vectors.md"));
    Files.writeString(file2, """
        # This is vectors.md content
        [[Other content]]
        Some other non important text
        :::Another question:::
        """);

    // Adding temporary contents to a file in order to check if it can be read
    ArrayList<File> fileArrayList = new ArrayList<>();
    fileArrayList.add(file1.toFile());
    fileArrayList.add(file2.toFile());
    wmf = new WriteMarkdownFile(fileArrayList);
  }

  /**
   * Tests checking if it is possible to write to a non-existing Path/File in a directory
   */
  @Test
  void testWriteToFileInvalidPath() {
    // Creating a File object resolved against tempDir converting the path to a string
    // with an invalid path as it begins with //
    File invalidPathFile = new File(tempDir.resolve("//path/output.md").toString());
    assertThrows(IllegalArgumentException.class, () ->
        wmf.writeToFile(invalidPathFile, "Content trying to be "
            + "written to file", ".md"));
  }

  /**
   * Test checking the output of getRightContent method
   */
  @Test
  void testGetRightContent() {
    // temp output file in the temp directory
    File tempFile1 = new File(tempDir.resolve("output.md").toString());

    // Call getRightContent with outputFile
    ArrayList<String> result = wmf.getRightContent(tempFile1);

    // Check that the getRightContent method works correctly
    assertEquals("""
        # This is arrays.md content
        - Some content

        # This is vectors.md content
        - Other content
        """, result.get(0));
    assertEquals(":::Some question:::\n:::Another question:::\n", result.get(1));
  }

  /**
   * To check if we can write to file if there is an I/O exception
   *
   * @throws IOException the correct exception if we were not to have permission
   */
  @Test
  void testWriteToFileNoPermission() throws IOException {
    // Create a read-only file
    Path readOnlyFile = Files.createFile(tempDir.resolve("readonly.md"));
    readOnlyFile.toFile().setReadOnly();

    // Try to write to the read-only file
    assertThrows(RuntimeException.class, () ->
        wmf.writeToFile(readOnlyFile.toFile(), "Some content", ".md"));
  }
}