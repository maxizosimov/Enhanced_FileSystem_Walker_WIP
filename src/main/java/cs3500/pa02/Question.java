package cs3500.pa02;

/**
 * represents a question object from the .sr files
 */
public class Question {

  private final String questionText;
  private String difficulty;
  private final String answer;

  /**
   * constructor for Question obj
   *
   * @param question represents a split question from .sr file -- question / answer / data
   */
  public Question(String[] question) { // split our question to array question - answer - metadata
    this.questionText = question[0];
    this.answer = question[1];
    this.difficulty = question[2];
  }

  /**
   * Helper method (getter) to get this text from a question
   *
   * @return the text from this question
   */
  public String getQuestionText() {
    return this.questionText;
  }

  /**
   * Helper method (setter) to set this metadata (difficulty) in a question
   *
   * @param difficulty represents the metadata (difficulty) in a question
   */
  public void setDifficulty(String difficulty) {
    this.difficulty = difficulty;
  }

  /**
   * Helper method (getter) to get this metadata (difficulty) from a question
   *
   * @return the difficulty of this question
   */
  public String getDifficulty() {
    return this.difficulty;
  }

  /**
   * Helper method (getter) to get this answer from a question
   *
   * @return the answer from this question
   */
  public String getAnswer() {
    return answer;
  }
}
