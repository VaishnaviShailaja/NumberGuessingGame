import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Number Guessing Game (console version)
 *
 * Features:
 *  - Random number generated at the start of each round
 *  - "Too High!", "Too Low!", "Correct!" feedback
 *  - Visible attempt counter
 *  - Maximum attempts, with "You Lost!" and the number revealed
 *  - Play Again prompt
 *  - Score tracking: "Round X - guessed in Y attempts"
 *  - Bonus: Easy / Medium / Hard difficulty levels
 *
 * Compile: javac NumberGuessingGame.java
 * Run:     java NumberGuessingGame
 */
public class NumberGuessingGame {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    // Difficulty levels: label, upper bound of range, max attempts
    private enum Difficulty {
        EASY("Easy", 50, 10),
        MEDIUM("Medium", 100, 7),
        HARD("Hard", 200, 5);

        final String label;
        final int max;
        final int maxAttempts;

        Difficulty(String label, int max, int maxAttempts) {
            this.label = label;
            this.max = max;
            this.maxAttempts = maxAttempts;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== NUMBER GUESSING GAME ===");

        List<String> history = new ArrayList<>();
        int round = 0;
        boolean playAgain = true;

        while (playAgain) {
            round++;
            Difficulty difficulty = chooseDifficulty();
            int attemptsUsed = playRound(difficulty);

            if (attemptsUsed > 0) {
                history.add("Round " + round + " [" + difficulty.label + "] - guessed in "
                        + attemptsUsed + (attemptsUsed == 1 ? " attempt" : " attempts"));
            } else {
                history.add("Round " + round + " [" + difficulty.label + "] - not guessed (lost)");
            }

            printScoreboard(history);
            playAgain = askPlayAgain();
        }

        System.out.println("\nThanks for playing! Final results:");
        printScoreboard(history);
    }

    /** Lets the user pick a difficulty level. */
    private static Difficulty chooseDifficulty() {
        System.out.println("\nChoose a difficulty:");
        Difficulty[] levels = Difficulty.values();
        for (int i = 0; i < levels.length; i++) {
            Difficulty d = levels[i];
            System.out.printf("  %d) %-6s (1-%d, %d attempts)%n",
                    i + 1, d.label, d.max, d.maxAttempts);
        }
        int choice = readInt("Enter 1-" + levels.length + ": ", 1, levels.length);
        return levels[choice - 1];
    }

    /**
     * Plays a single round.
     * @return number of attempts used if the user won, or -1 if they lost
     */
    private static int playRound(Difficulty difficulty) {
        int secret = random.nextInt(difficulty.max) + 1; // 1..max
        int attempts = 0;

        System.out.printf("%nI'm thinking of a number between 1 and %d. You have %d attempts.%n",
                difficulty.max, difficulty.maxAttempts);

        while (attempts < difficulty.maxAttempts) {
            System.out.printf("Attempt %d of %d%n", attempts + 1, difficulty.maxAttempts);
            int guess = readInt("Your guess: ", 1, difficulty.max);
            attempts++;

            if (guess == secret) {
                System.out.println("Correct!");
                System.out.println("You got it in " + attempts
                        + (attempts == 1 ? " attempt." : " attempts."));
                return attempts;
            } else if (guess > secret) {
                System.out.println("Too High!");
            } else {
                System.out.println("Too Low!");
            }
        }

        System.out.println("You Lost! The number was " + secret + ".");
        return -1;
    }

    /** Reads an integer within [min, max], re-prompting on invalid input. */
    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            if (!scanner.hasNextLine()) { // input closed (e.g., Ctrl+D)
                System.out.println("\nNo more input. Goodbye!");
                System.exit(0);
            }
            String line = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("That's not a valid number. Try again.");
            }
        }
    }

    /** Asks a yes/no question; keeps asking until the answer is valid. */
    private static boolean askPlayAgain() {
        while (true) {
            System.out.print("\nPlay again? (yes/no): ");
            if (!scanner.hasNextLine()) {
                return false;
            }
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                return true;
            } else if (answer.equals("no") || answer.equals("n")) {
                return false;
            }
            System.out.println("Please type yes or no.");
        }
    }

    /** Prints the round-by-round summary. */
    private static void printScoreboard(List<String> history) {
        System.out.println("\n--- Scoreboard ---");
        for (String entry : history) {
            System.out.println(entry);
        }
        System.out.println("------------------");
    }
}