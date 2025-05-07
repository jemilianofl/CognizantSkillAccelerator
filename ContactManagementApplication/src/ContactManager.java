public class ContactManager {
    public static void main(String[] args) {
        ContactDirectory directory = new ContactDirectory();

        System.out.println("--- 1. Adding New Contacts ---");
        Contact c1 = new Contact("C001", "Alice Wonderland", "123-456-7890", "alice@example.com", "Personal");
        Contact c2 = new Contact("C002", "Bob The Builder", "987-654-3210", "bob@example.com", "Professional");
        Contact c3 = new Contact("C003", "Charlie Brown", "555-123-4567", "charlie@example.com", "Personal");
        Contact c4 = new Contact("C004", "Diana Prince", "777-888-9999", "diana@example.com", "Professional");

        directory.addContact(c1);
        directory.addContact(c2);
        directory.addContact(c3);
        directory.addContact(c4);

        directory.displayAllContacts();

        System.out.println("\n--- 2. Registering and Displaying Unique Contact Types ---");
        // Types are added automatically when contacts are added, but we can also add them explicitly
        directory.addContactType("Personal"); // Will show it already exists if added via contact
        directory.addContactType("Professional"); // Same
        directory.addContactType("Family");
        directory.addContactType("Friend");
        directory.addContactType("Family"); // Demonstrate duplicate prevention

        directory.displayContactTypes(); // Will show Personal, Professional, Family, Friend

        System.out.println("\n--- 3. Searching for a Contact by Name ---");
        String searchName = "Bob The Builder";
        Contact foundContact = directory.searchContactByName(searchName);
        if (foundContact != null) {
            System.out.println("Found: " + foundContact);
        } else {
            System.out.println("Contact '" + searchName + "' not found.");
        }

        String searchNameNotFound = "Eve Missing";
        Contact notFoundContact = directory.searchContactByName(searchNameNotFound);
        if (notFoundContact != null) {
            System.out.println("Found: " + notFoundContact);
        } else {
            System.out.println("Contact '" + searchNameNotFound + "' not found.");
        }

        System.out.println("\n--- 4. Updating Contact Information ---");
        // Update Charlie Brown's email and phone
        directory.updateContact("Charlie Brown", "Charlie Brown", "555-999-8888", "cbrown.new@example.com", "Personal Friend");
        // directory.updateContact("Charlie Brown", new Contact("C003", "Charlie Brown", "555-999-8888", "cbrown.new@example.com", "Personal Friend"));
        // Note: The above commented line would also work with the overloaded method.
        // The contactType "Personal Friend" will be added to uniqueContactTypes if it's new or update the existing "Personal".
        // For this example, it makes "Personal Friend" a new distinct type.

        // Update Diana Prince's name and contact type
        directory.updateContact("Diana Prince", "Diana W. Prince", null, null, "Superhero");

        System.out.println("\n--- Displaying Contacts After Updates ---");
        directory.displayAllContacts();
        directory.displayContactTypes(); // Check if "Personal Friend" and "Superhero" were added


        System.out.println("\n--- 5. Sorting and Displaying Contacts Alphabetically ---");
        directory.sortContactsByName();
        directory.displayAllContacts();

        // Demonstrate trying to add a contact with an existing name
        System.out.println("\n--- Attempting to add a duplicate name contact ---");
        Contact c5_duplicate_name = new Contact("C005", "Alice Wonderland", "111-222-3333", "alice.new@example.com", "Personal");
        directory.addContact(c5_duplicate_name); // Should give an error or be handled

        // Demonstrate updating a contact to a name that already exists
        System.out.println("\n--- Attempting to update a contact to an existing name ---");
        directory.updateContact("Bob The Builder", "Alice Wonderland", "222-333-4444", "bob.new@example.com", "Work"); // Should give an error

    }
}