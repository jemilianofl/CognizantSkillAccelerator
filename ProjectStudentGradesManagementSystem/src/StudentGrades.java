import java.util.*;

public class StudentGrades {

        private static final int NUMBER_OF_STUDENTS = 10; // Fixed number of students

        public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);
            int[] grades = new int[NUMBER_OF_STUDENTS];

            System.out.println("Welcome to the Student Grades Management System");
            System.out.println("---------------------------------------------");

            // Step 1: Input grades
            inputGrades(grades, scanner);

            // Step 2: Display all grades
            displayGrades(grades);

            // Step 3: Calculate the average grade
            double averageGrade = calculateAverage(grades);
            System.out.printf("\nAverage Grade: %.2f\n", averageGrade); // Formatted to 2 decimal places

            // Step 4: Find the highest grade
            int highestGrade = findHighestGrade(grades);
            System.out.println("Highest Grade: " + highestGrade);

            // Step 5: Find the lowest grade
            int lowestGrade = findLowestGrade(grades);
            System.out.println("Lowest Grade: " + lowestGrade);

            System.out.println("\n---------------------------------------------");
            System.out.println("Thank you for using the system.");

            scanner.close(); // Close the scanner
        }

        /**
         * Allows the user to input grades for each student and stores them in the array.
         * Includes basic input validation for grades between 0 and 100.
         *
         * @param grades  The array to store the grades.
         * @param scanner The Scanner object for user input.
         */
        public static void inputGrades(int[] grades, Scanner scanner) {
            System.out.println("\nPlease enter the grades for " + NUMBER_OF_STUDENTS + " students (0-100):");
            for (int i = 0; i < grades.length; i++) {
                boolean validInput = false;
                while (!validInput) {
                    try {
                        System.out.print("Enter grade for Student " + (i + 1) + ": ");
                        int grade = scanner.nextInt();
                        if (grade >= 0 && grade <= 100) {
                            grades[i] = grade;
                            validInput = true;
                        } else {
                            System.out.println("Invalid grade. Please enter a value between 0 and 100.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Invalid input. Please enter an integer value for the grade.");
                        scanner.next(); // Consume the invalid input to prevent an infinite loop
                    }
                }
            }
            scanner.nextLine(); // Consume the leftover newline after the last nextInt()
        }

        /**
         * Calculates the average of all grades in the array.
         *
         * @param grades The array of grades.
         * @return The average grade as a double. Returns 0.0 if the array is empty.
         */
        public static double calculateAverage(int[] grades) {
            if (grades == null || grades.length == 0) {
                return 0.0; // Avoid division by zero if array is empty or null
            }
            int sum = 0;
            for (int grade : grades) {
                sum += grade;
            }
            return (double) sum / grades.length;
        }

        /**
         * Finds the highest grade in the array.
         *
         * @param grades The array of grades.
         * @return The highest grade. Returns Integer.MIN_VALUE if the array is empty or null.
         */
        public static int findHighestGrade(int[] grades) {
            if (grades == null || grades.length == 0) {
                System.err.println("Cannot find highest grade: array is empty or null.");
                return Integer.MIN_VALUE; // Or throw an IllegalArgumentException
            }
            int highest = grades[0]; // Assume the first grade is the highest initially
            for (int i = 1; i < grades.length; i++) {
                if (grades[i] > highest) {
                    highest = grades[i];
                }
            }
            return highest;
        }

        /**
         * Finds the lowest grade in the array.
         *
         * @param grades The array of grades.
         * @return The lowest grade. Returns Integer.MAX_VALUE if the array is empty or null.
         */
        public static int findLowestGrade(int[] grades) {
            if (grades == null || grades.length == 0) {
                System.err.println("Cannot find lowest grade: array is empty or null.");
                return Integer.MAX_VALUE; // Or throw an IllegalArgumentException
            }
            int lowest = grades[0]; // Assume the first grade is the lowest initially
            for (int i = 1; i < grades.length; i++) {
                if (grades[i] < lowest) {
                    lowest = grades[i];
                }
            }
            return lowest;
        }

        /**
         * Displays all the grades stored in the array.
         *
         * @param grades The array of grades.
         */
        public static void displayGrades(int[] grades) {
            System.out.println("\n--- All Student Grades ---");
            if (grades == null || grades.length == 0) {
                System.out.println("No grades to display.");
                return;
            }
            for (int i = 0; i < grades.length; i++) {
                System.out.println("Student " + (i + 1) + ": " + grades[i]);
            }
        }
}