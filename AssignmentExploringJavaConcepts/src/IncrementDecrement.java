public class IncrementDecrement {

    public static void main(String[] args) {
        // Declaration and initialization of the counter variable
        int counter = 10;

        // Increment operator
        counter++; // Increments the counter by 1
        System.out.println("Counter value after incrementing: " + counter);

        // Decrement operator
        counter--; // Decrements the counter by 1
        System.out.println("Counter value after decrementing: " + counter);

        // Counter increment using for loop
        System.out.println("\nIncrementing counter from 10 to 15 using for-loop:");
        for (int i = 10; i <= 15; i++) {
            System.out.println("Contador: " + i);
        }

        // Decremento de contador usando bucle while
        System.out.println("\nDecrementing counter from 15 to 10 using while loop:");
        int j = 15;
        while (j >= 10) {
            System.out.println("Counter: " + j);
            j--;
        }
    }

}
