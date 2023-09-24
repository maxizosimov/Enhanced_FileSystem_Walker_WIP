package cs3500.pa02;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * This is the main driver of this project.
 */
public class Driver {
  /**
   * Project entry point
   *
   * @param args - to check # of cmdline args to now which mode to proceed to
   */
  public static void main(String[] args) throws IOException {
    if (args.length > 0) {
      createStudyGuides(args);
    } else {
      createStudySession();
    }
  }

  /**
   * mode where we create a studyGuide .md
   *
   * @param input represents the cmd line args
   * @throws IOException throws if we reach an I/O error
   */
  static void createStudyGuides(String[] input) throws IOException {
    File dirArg = new File(input[0]);
    String orderArg = input[1];
    File outputArg = new File(input[2]);

    // walks the tree when given the directory and output target
    ReadMarkdownFiles rmd = new ReadMarkdownFiles();
    rmd.treeWalker(dirArg, outputArg);
    // sorts the files (and their contents) after a given cmdline (an ordering flag)
    SortingMarkdownFile smf = new SortingMarkdownFile(rmd.getFiles());
    ArrayList<File> sortedFiles = smf.orderingFlag(orderArg.toLowerCase());
    // writes content of files to the output file in, ordered in the way given above
    WriteMarkdownFile wmf = new WriteMarkdownFile(sortedFiles);
    ArrayList<String> fileContent = wmf.getRightContent(outputArg);

    // Used in order to catch if the cmdline arguments are wrong
    try {
      wmf.writeToFile(outputArg, fileContent.get(0), ".md");
      wmf.writeToFile(outputArg, fileContent.get(1), ".sr");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * mode where we create a make a .sr question bank and start generating/displaying questions
   *
   * @throws IOException throws if we reach an I/O error
   */
  static void createStudySession() throws IOException {
    Scanner input = new Scanner(System.in);

    System.out.println("Welcome to this study session!\n");

    System.out.println("Please supply the full path to the question bank: ");

    String questionPath = input.nextLine();

    while (!Files.exists(Path.of(questionPath))) {
      System.out.println("Please provide a valid path to the question bank");
      questionPath = input.nextLine();
    }

    System.out.println("How many questions do you want to practice");

    int inputQuestionAmount;

    while (true) {
      try {
        inputQuestionAmount = input.nextInt();
        break; // will only get to here if input was an int. otherwise, the exception will be thrown
      } catch (InputMismatchException e) {
        System.out.println("Please provide a number of questions, no other characters please!");
        input.next(); // discard non-integer input
      }
    }

    QuestionHandler questionHandler = new QuestionHandler(questionPath, inputQuestionAmount);

    questionHandler.generateQuestions();
  }
}

//"questionBankFolder" "filename" "questionBankFolder/tester/studyGuide.md"