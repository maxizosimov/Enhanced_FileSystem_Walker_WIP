package cs3500.pa02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

/**
 * represents a class that handles questions and game states for a study session
 */
public class QuestionHandler {

  final int questionAmount;

  final String questionPath;

  int totalAnswered = 0;

  int easyToHard = 0;

  int hardToEasy = 0;

  int totalEasy = 0;

  int totalHard = 0;

  private Scanner scanner;


  // main constructor
  public QuestionHandler(String questionPath, int questionAmount) {
    this.questionPath = questionPath;
    this.questionAmount = questionAmount;
  }

  /**
   * constructor for testing
   *
   * @param questionPath represents the path to a question
   * @param questionAmount represents the amount of questions a user wants in a study session
   * @param scanner Used for testing purposes to take input from user
   */
  public QuestionHandler(String questionPath, int questionAmount, Scanner scanner) {
    this.questionPath = questionPath;
    this.questionAmount = questionAmount;
    this.scanner = scanner;
  }

  /**
   * generates randomly selected questions from the question bank
   *
   * @throws IOException the correct exception if an I/O error occurs
   */
  public void generateQuestions() throws IOException {
    Path filePath = Path.of(questionPath);
    String fileContent = Files.readString(filePath);
    List<String> srContent = Arrays.asList(fileContent.split("\n"));
    Collections.shuffle(srContent);
    ArrayList<Question> questions = new ArrayList<>();

    // if user asks for more than we have in bank, we iterate through the length of bank
    // if user asks for less than what we have in bank, we iterate through size of requested amount

    // iterating through our question bank
    for (int i = 0; i < Math.min(questionAmount, srContent.size()); i++) {
      // to generate random question from question bank
      // manually removing [[... from question
      String str = srContent.get(i).replaceAll("\\[\\[", "");

      // splits question into [0] ([[...) - answer [1] (...]]) - [2] metadata
      Question question = new Question(str.split(":::|]]"));
      questions.add(question);
    }
    questions.sort(new Comparator<Question>() {
      @Override
      public int compare(Question q1, Question q2) {
        return q2.getDifficulty().compareTo(q1.getDifficulty());
      }
    });

    // If there are no questions
    if (questions.size() == 0) {
      throw new IllegalArgumentException("No questions available");
    } else {
      // displays the questions from the question bank list
      displayQuestions(questions, scanner);
    }
  }

  /**
   * displays the questions
   *
   * @param questions our list of questions from the question bank
   * @param scanner is for testing with input instead of using a Mock
   */
  public void displayQuestions(ArrayList<Question> questions, Scanner scanner) {
    // user input
    scanner = new Scanner(System.in);

    int counter = 0;

    // iterating through our question bank to retrieve questions
    //for (Question question : questions) {
    while (counter < questions.size()) {

      Question question = questions.get(counter);

      System.out.println(question.getQuestionText());
      System.out.println("Please input your answer:");
      System.out.println("""
            1: Easy
            2: Hard
            3: Show Answer
            4: Exit""");

      String response = scanner.nextLine();

      switch (response) {
        case "1":
          if (!question.getDifficulty().equals("Difficulty: Easy")) {
            hardToEasy++;
            totalEasy++;
            totalAnswered++;
            if (totalHard > 0) {
              totalHard--;
            }
          }
          question.setDifficulty("Difficulty: Easy");
          break;
        case "2":
          if (!question.getDifficulty().equals("Difficulty: Hard")) {
            easyToHard++;
            totalHard++;
            totalAnswered++;
            if (totalEasy > 0) {
              totalEasy--;
            }
          }
          question.setDifficulty("Difficulty: Hard");
          break;
        case "3":
          System.out.println(question.getAnswer() + "\n");
          totalAnswered++; // should it be answered if shown or not?
          break;
        case "4":
          counter = questions.size() + 1;
          break;
        default:
          // in case user types something else than e or h to update the difficulty
          throw new IllegalArgumentException("Invalid response. "
              + "Please enter 'e' for Easy or 'h' for Hard.");
      }
      counter++;
    }
    // scanner closing
    scanner.close();
    // display session stats
    displayStats();
  }

  /**
   * displays the stats of the session to the user
   */
  void displayStats() {
    System.out.println("Total questions answered: " + totalAnswered);
    System.out.println("Total questions that changed from easy to hard: " + easyToHard);
    System.out.println("Total questions that changed from hard to easy: " + hardToEasy);
    System.out.println("Updated total number of hard questions: " + totalHard);
    System.out.println("Updated total number of easy questions: " + totalEasy);
  }

  /**
   * Getter method for total answered used for testing
   *
   * @return amount of total answered questions
   */
  public int getTotalAnswered() {
    return totalAnswered;
  }

  /**
   * Getter method for total # of questions gone from easy to hard used for testing
   *
   * @return amount of total questions gone from easy to hard
   */
  public int getEasyToHard() {
    return easyToHard;
  }

  /**
   * Getter method for total # of questions gone from hard to easy used for testing
   *
   * @return amount of total questions gone from hard to easy
   */
  public int getHardToEasy() {
    return hardToEasy;
  }

  /**
   * Getter method for total easy questions used for testing
   *
   * @return amount of total easy questions
   */
  public int getTotalEasy() {
    return totalEasy;
  }

  /**
   * Getter method for total hard questions used for testing
   *
   * @return amount of total hard questions
   */
  public int getTotalHard() {
    return totalHard;
  }
}