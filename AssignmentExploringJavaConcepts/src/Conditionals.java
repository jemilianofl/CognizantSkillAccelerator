public class Conditionals {

    public static void main(String[] args) {

        // Declaration and initialization of variables for test scores
        int score1 = 85; // Change this value to a number between 0 and 100
        int score2 = 92; // Change this value to a number between 0 and 100
        int score3 = 78; // Change this value to a number between 0 and 100

        // Calculate the average of the three scores
        int averageScore = (score1 + score2 + score3) / 3;
        System.out.println("The average score is: " + averageScore);

        // Use conditionals and logical operators to classify the average
        if (averageScore >= 90 && averageScore <= 100) {
            System.out.println("Classification: Excellent");
        } else if (averageScore >= 70 && averageScore <= 89) {
            System.out.println("Classification: Good");
        } else if (averageScore >= 50 && averageScore <= 69) {
            System.out.println("Classification: Average");
        } else {
            System.out.println("Classification: Poor");
        }

        // Use a switch statement to print the day of the week
        int day = 3; // Change this value to a number between 1 and 7

        System.out.print("Day " + day + " of the week is: ");
        switch (day) {
            case 1:
                System.out.println("Monday");
                break;
            case 2:
                System.out.println("Tuesday");
                break;
            case 3:
                System.out.println("Wednesday");
                break;
            case 4:
                System.out.println("Thursday");
                break;
            case 5:
                System.out.println("Friday");
                break;
            case 6:
                System.out.println("Saturday");
                break;
            case 7:
                System.out.println("Sunday");
                break;
            default:
                System.out.println("Invalid day. Must be a number between 1 and 7.");
                break;
        }

    }

}
