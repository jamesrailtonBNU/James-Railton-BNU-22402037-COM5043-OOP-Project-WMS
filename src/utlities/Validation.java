package utlities;

import java.util.Scanner;

public final class Validation {
    private static final Scanner scanner = new Scanner(System.in);

    //Validation to handle integer input to ensure only a whole number is entered
    public static int getValidatedInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    // Validation to handle double input to ensure only a valid number is entered
    public static double getValidatedDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    public static String getValidatedString(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("Input cannot be empty. Please try again.");
            }
        }
    }

    public static String getOptionalString(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

}

