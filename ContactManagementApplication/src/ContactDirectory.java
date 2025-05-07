import java.util.*;
public class ContactDirectory {
    // List Implementation: Store all contacts
    private List<Contact> allContacts;

    // Set Implementation: Maintain unique contact types
    private Set<String> uniqueContactTypes;

    // Map Implementation: Organize contacts by name for quick search
    private Map<String, Contact> contactsByName;

    public ContactDirectory() {
        this.allContacts = new ArrayList<>();
        this.uniqueContactTypes = new HashSet<>();
        this.contactsByName = new HashMap<>();
    }

    // --- List Operations ---
    public void addContact(Contact contact) {
        if (contact == null) {
            System.out.println("Cannot add a null contact.");
            return;
        }
        if (contactsByName.containsKey(contact.getName())) {
            System.out.println("Error: Contact with name '" + contact.getName() + "' already exists. " +
                    "Contact IDs must be unique if names are the same, or use update functionality.");
            // Or, if contactId is the true unique identifier, you might check that instead
            // For this problem, name is the key in the map, so we'll treat name as a unique lookup key.
            return;
        }

        this.allContacts.add(contact);
        this.contactsByName.put(contact.getName(), contact);
        // Automatically add the contact's type to the set of unique types
        if (contact.getContactType() != null && !contact.getContactType().isEmpty()) {
            this.uniqueContactTypes.add(contact.getContactType());
        }
        System.out.println("Contact added: " + contact.getName());
    }

    public void displayAllContacts() {
        if (allContacts.isEmpty()) {
            System.out.println("Contact list is empty.");
            return;
        }
        System.out.println("\n--- All Contacts ---");
        for (Contact contact : allContacts) {
            System.out.println(contact);
        }
    }

    public void sortContactsByName() {
        Collections.sort(allContacts); // Uses the compareTo method in Contact class
        System.out.println("\nContacts sorted by name.");
    }

    // --- Set Operations ---
    public void addContactType(String contactType) {
        if (contactType == null || contactType.trim().isEmpty()) {
            System.out.println("Cannot add an empty contact type.");
            return;
        }
        boolean added = uniqueContactTypes.add(contactType);
        if (added) {
            System.out.println("Contact type '" + contactType + "' added.");
        } else {
            System.out.println("Contact type '" + contactType + "' already exists. Not added again.");
        }
    }

    public void displayContactTypes() {
        if (uniqueContactTypes.isEmpty()) {
            System.out.println("No contact types available.");
            return;
        }
        System.out.println("\n--- Unique Contact Types ---");
        for (String type : uniqueContactTypes) {
            System.out.println(type);
        }
    }

    // --- Map Operations ---
    public Contact searchContactByName(String name) {
        return contactsByName.get(name);
    }

    public void updateContact(String oldName, String newName, String newPhoneNumber, String newEmail, String newContactType) {
        Contact contactToUpdate = contactsByName.get(oldName);

        if (contactToUpdate == null) {
            System.out.println("Contact with name '" + oldName + "' not found. Cannot update.");
            return;
        }

        // If the name is changing, we need to update the map's key
        if (newName != null && !newName.isEmpty() && !oldName.equals(newName)) {
            if (contactsByName.containsKey(newName)) {
                System.out.println("Error: Another contact with the new name '" + newName + "' already exists. Update failed.");
                return;
            }
            // Remove old entry from map, update name, then add new entry to map
            contactsByName.remove(oldName);
            contactToUpdate.setName(newName);
            contactsByName.put(newName, contactToUpdate); // The object in allContacts list is also updated
        }

        if (newPhoneNumber != null && !newPhoneNumber.isEmpty()) {
            contactToUpdate.setPhoneNumber(newPhoneNumber);
        }
        if (newEmail != null && !newEmail.isEmpty()) {
            contactToUpdate.setEmail(newEmail);
        }
        if (newContactType != null && !newContactType.isEmpty()) {
            contactToUpdate.setContactType(newContactType);
            uniqueContactTypes.add(newContactType); // Ensure new type is in the set
        }

        // Note: The object in allContacts list is the same instance, so it's already updated.
        // If we weren't updating by reference, we'd need to find and replace in allContacts too.
        // For simplicity, since name is our key and might change, handling the map is crucial.
        // The object in allContacts has its attributes changed directly.

        System.out.println("Contact '" + (newName != null && !newName.isEmpty() ? newName : oldName) + "' updated successfully.");
    }

    // Overloaded updateContact to take a Contact object with new details (except potentially ID)
    public void updateContact(String nameToFind, Contact updatedInfo) {
        Contact contactToUpdate = contactsByName.get(nameToFind);

        if (contactToUpdate == null) {
            System.out.println("Contact with name '" + nameToFind + "' not found. Cannot update.");
            return;
        }

        // Handle name change specifically for map key
        if (!nameToFind.equals(updatedInfo.getName())) {
            if (contactsByName.containsKey(updatedInfo.getName())) {
                System.out.println("Error: Another contact with the new name '" + updatedInfo.getName() + "' already exists. Update failed.");
                return;
            }
            contactsByName.remove(nameToFind); // Remove old key
            contactToUpdate.setName(updatedInfo.getName());
            contactsByName.put(updatedInfo.getName(), contactToUpdate); // Add with new key
        }

        // Update other fields
        contactToUpdate.setPhoneNumber(updatedInfo.getPhoneNumber());
        contactToUpdate.setEmail(updatedInfo.getEmail());
        contactToUpdate.setContactType(updatedInfo.getContactType());

        // Ensure the new contact type is in the set
        if (updatedInfo.getContactType() != null && !updatedInfo.getContactType().isEmpty()) {
            uniqueContactTypes.add(updatedInfo.getContactType());
        }

        System.out.println("Contact '" + updatedInfo.getName() + "' updated successfully.");
    }
}