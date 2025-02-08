package fr.flylonyx.jdorm.example;

import fr.flylonyx.jdorm.example.migrations.AddPhoneNumberToPersons;
import fr.flylonyx.jdorm.example.migrations.CreateAddressesTable;
import fr.flylonyx.jdorm.example.migrations.CreatePersonsTable;
import fr.flylonyx.jdorm.example.migrations.ModifySalaryColumnInPersons;
import fr.flylonyx.jdorm.example.models.Address;
import fr.flylonyx.jdorm.example.models.Person;
import fr.flylonyx.jdorm.library.core.MigrationManager;
import fr.flylonyx.jdorm.library.core.Schema;
import fr.flylonyx.jdorm.library.database.Connection;
import fr.flylonyx.jdorm.library.utils.Operations;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        Connection.configure("jdbc:mysql://", "localhost", "my_database", "my_user", "my_password");
        java.sql.Connection connection = Connection.getConnection();
        Schema schema = new Schema(connection);

        MigrationManager.initialize();

        MigrationManager.applyMigration("CreatePersonsTable", () -> {
            try {
                CreatePersonsTable.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        MigrationManager.applyMigration("AddPhoneNumberToPersons", () -> {
            try {
                AddPhoneNumberToPersons.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        MigrationManager.applyMigration("CreateAddressesTable", () -> {
            try {
                CreateAddressesTable.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        MigrationManager.applyMigration("ModifySalaryColumnInPersons", () -> {
            try {
                ModifySalaryColumnInPersons.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Creating a person
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setEmail("john.doe@example.com");
        person1.setAge(30);
        person1.setActive(true);
        person1.setSalary(50000.00);
        person1.setBirthDate("1990-01-01");
        person1.setCreatedAt("2023-10-01 12:00:00");
        person1.save();

        // Creating another person
        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setEmail("jane.smith@example.com");
        person2.setAge(25);
        person2.setActive(false);
        person2.setSalary(60000.00);
        person2.setBirthDate("1995-05-05");
        person2.setCreatedAt("2023-10-02 14:00:00");
        person2.save();

        // Creating an address for the first person
        Address address1 = new Address();
        address1.setStreet("123 Main St");
        address1.setCity("New York");
        address1.setPersonId(person1.getId());
        address1.save();

        // Creating an address for the second person
        Address address2 = new Address();
        address2.setStreet("456 Elm St");
        address2.setCity("Los Angeles");
        address2.setPersonId(person2.getId());
        address2.save();

        // Updating a person
        person1.setSalary(55000.00);
        person1.update();

        // Deleting a person
        person2.delete();

        // Displaying the list of addresses of a person
        List<Address> addresses = person1.getAddresses();
        addresses.forEach(System.out::println);

        // Finding a person by their ID
        Person foundPerson = Person.findById(1, Person.class);
        if (foundPerson != null) {
            System.out.println("Found person by ID: " + foundPerson.getFirstName() + " " + foundPerson.getLastName());
        } else {
            System.out.println("Person not found.");
        }

        // Retrieving all active persons
        List<Person> activePersons = Person.query(Person.class)
                .where("is_active", Operations.EQUALS, true)
                .execute();
        System.out.println("Active Persons: " + activePersons);

        // Retrieving a person by their email
        Person personByEmail = Person.query(Person.class)
                .where("email", Operations.EQUALS, "john.doe@example.com")
                .first();
        System.out.println("Person by Email: " + personByEmail);

        // Retrieving persons with a salary greater than 50000
        List<Person> highSalaryPersons = Person.query(Person.class)
                .where("salary", Operations.EQUALS, 50000)
                .execute();
        System.out.println("High Salary Persons: " + highSalaryPersons);

        // Retrieving persons with a salary between 40000 and 60000
        List<Person> midSalaryPersons = Person.query(Person.class)
                .whereBetween("salary", 40000, 60000)
                .execute();
        System.out.println("Mid Salary Persons: " + midSalaryPersons);

        // Retrieving persons with a specific age from a list
        List<Person> specificAgePersons = Person.query(Person.class)
                .whereIn("age", Arrays.asList(25, 30))
                .execute();
        System.out.println("Specific Age Persons: " + specificAgePersons);

        // Retrieving persons sorted by last name
        List<Person> sortedPersons = Person.query(Person.class)
                .orderBy("last_name")
                .execute();
        System.out.println("Sorted Persons: " + sortedPersons);

        // Retrieving persons sorted in descending order by salary
        List<Person> sortedDescPersons = Person.query(Person.class)
                .orderByDesc("salary")
                .execute();
        System.out.println("Sorted Desc Persons: " + sortedDescPersons);

        // Retrieving a limited number of persons
        List<Person> limitedPersons = Person.query(Person.class)
                .limit(1)
                .execute();
        System.out.println("Limited Persons: " + limitedPersons);

        // Retrieving persons with an offset
        List<Person> offsetPersons = Person.query(Person.class)
                .offset(1)
                .execute();
        System.out.println("Offset Persons: " + offsetPersons);

        // Retrieving the addresses of a specific person
        List<Address> personAddresses = Address.query(Address.class)
                .where("person_id", Operations.EQUALS, person1.getId())
                .execute();
        System.out.println("Person Addresses: " + personAddresses);

        // Retrieving addresses with a join on the persons table
        List<Address> addressesWithJoin = Address.query(Address.class)
                .join("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Join: " + addressesWithJoin);

        // Retrieving addresses with a left join on the persons table
        List<Address> addressesWithLeftJoin = Address.query(Address.class)
                .leftJoin("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Left Join: " + addressesWithLeftJoin);

        // Retrieving addresses with a right join on the persons table
        List<Address> addressesWithRightJoin = Address.query(Address.class)
                .rightJoin("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Right Join: " + addressesWithRightJoin);

        // Retrieving addresses grouped by city
        List<Address> addressesGroupedByCity = Address.query(Address.class)
                .groupBy("city")
                .execute();
        System.out.println("Addresses Grouped by City: " + addressesGroupedByCity);

        // Retrieving addresses with a HAVING condition
        List<Address> addressesHavingCondition = Address.query(Address.class)
                .groupBy("city")
                .having("COUNT(city)", Operations.GREATER_THAN, 1)
                .execute();
        System.out.println("Addresses with HAVING Condition: " + addressesHavingCondition);

        // Deleting all persons that meet a certain criteria (e.g., age greater than 30)
        Person.query(Person.class)
                .execute()
                .stream()
                .filter(p -> p.getAge() > 30)
                .forEach(p -> {
                    try {
                        p.delete();
                        System.out.println("Deleted person: " + p.getFirstName() + " " + p.getLastName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

    }
}