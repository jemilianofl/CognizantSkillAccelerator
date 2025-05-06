public class ArrayAssignments {

    public static void main(String[] args) {

        // --- Part 2: Exercise ---
        System.out.println("--- Part 2: Exercise ---");
        // Declare and initialize an array of 5 double values.
        double[] doubleArray = new double[5];

        // Assign values to each element
        doubleArray[0] = 1.1;
        doubleArray[1] = 2.2;
        doubleArray[2] = 3.3;
        doubleArray[3] = 4.4;
        doubleArray[4] = 5.5;

        // Print them
        System.out.println("Elements of doubleArray:");
        // Using a for-each loop to print
        for (double value : doubleArray) {
            System.out.println(value);
        }
        // Or using a traditional for loop:
        // for (int i = 0; i < doubleArray.length; i++) {
        //     System.out.println("Element at index " + i + ": " + doubleArray[i]);
        // }
        System.out.println("----------------------------\n");


        // --- Part 3: Exercise ---
        System.out.println("--- Part 3: Exercise ---");
        // Create an array of 5 integers.
        int[] integerArray = new int[5];

        // Initialize the array with values.
        // You can do this directly or using a loop.
        // Direct initialization:
        // integerArray[0] = 10;
        // integerArray[1] = 20;
        // integerArray[2] = 30;
        // integerArray[3] = 40;
        // integerArray[4] = 50;

        // Or, using the combined declaration and initialization:
        int[] anotherIntegerArray = {10, 20, 30, 40, 50}; // Using this for the exercise

        // Update the third element to a new value.
        // Remember, the third element is at index 2 (0-based indexing).
        System.out.println("Original value of the third element (index 2): " + anotherIntegerArray[2]);
        anotherIntegerArray[2] = 100;
        System.out.println("New value of the third element (index 2): " + anotherIntegerArray[2]);

        // Print all elements of the array.
        System.out.println("All elements of anotherIntegerArray after update:");
        for (int i = 0; i < anotherIntegerArray.length; i++) {
            System.out.println("Element at index " + i + ": " + anotherIntegerArray[i]);
        }
        System.out.println("----------------------------\n");


        // --- Part 4: Exercise ---
        System.out.println("--- Part 4: Exercise ---");
        // Create an array of 10 integers.
        int[] multiplesArray = new int[10];

        // Fill the array with multiples of 3.
        // The first multiple of 3 is 3 (3*1), second is 6 (3*2), etc.
        for (int i = 0; i < multiplesArray.length; i++) {
            multiplesArray[i] = 3 * (i + 1); // (i+1) because indices start at 0
        }

        // Print the length of the array.
        System.out.println("The length of the multiplesArray is: " + multiplesArray.length);

        // Print all its elements.
        System.out.println("Elements of multiplesArray (multiples of 3):");
        for (int i = 0; i < multiplesArray.length; i++) {
            System.out.println("Element at index " + i + ": " + multiplesArray[i]);
        }
        System.out.println("----------------------------\n");
    }

}