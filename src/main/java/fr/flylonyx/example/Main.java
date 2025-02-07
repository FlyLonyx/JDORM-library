package fr.flylonyx.example;

import fr.flylonyx.example.migrations.AddPhoneNumberToPersons;
import fr.flylonyx.example.migrations.CreateAddressesTable;
import fr.flylonyx.example.migrations.CreatePersonsTable;
import fr.flylonyx.example.migrations.ModifySalaryColumnInPersons;
import fr.flylonyx.example.models.Address;
import fr.flylonyx.example.models.Person;
import fr.flylonyx.library.core.MigrationManager;
import fr.flylonyx.library.core.Schema;
import fr.flylonyx.library.database.Connection;
import fr.flylonyx.library.utils.Operations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception, SQLException {

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

        // Création d'une personne
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

        // Création d'une autre personne
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

        // Création d'une adresse pour la première personne
        Address address1 = new Address();
        address1.setStreet("123 Main St");
        address1.setCity("New York");
        address1.setPersonId(person1.getId());
        address1.save();

        // Création d'une adresse pour la deuxième personne
        Address address2 = new Address();
        address2.setStreet("456 Elm St");
        address2.setCity("Los Angeles");
        address2.setPersonId(person2.getId());
        address2.save();

        // Mise à jour d'une personne
        person1.setSalary(55000.00);
        person1.update();

        // Suppression d'une personne
        person2.delete();

        // Afficher la liste des adresses d'une personne
        List<Address> addresses = person1.getAddresses();
        addresses.forEach(System.out::println);

        // Trouver une personne par son id
        Person foundPerson = Person.findById(1, Person.class);
        if (foundPerson != null) {
            System.out.println("Found person by ID: " + foundPerson.getFirstName() + " " + foundPerson.getLastName());
        } else {
            System.out.println("Person not found.");
        }

        // Récupérer toutes les personnes actives
        List<Person> activePersons = Person.query(Person.class)
                .where("is_active", Operations.EQUALS, true)
                .execute();
        System.out.println("Active Persons: " + activePersons);

        // Récupérer une personne par son email
        Person personByEmail = Person.query(Person.class)
                .where("email", Operations.EQUALS, "john.doe@example.com")
                .first();
        System.out.println("Person by Email: " + personByEmail);

        // Récupérer les personnes avec un salaire supérieur à 50000
        List<Person> highSalaryPersons = Person.query(Person.class)
                .where("salary", Operations.EQUALS, 50000)
                .execute();
        System.out.println("High Salary Persons: " + highSalaryPersons);

        // Récupérer les personnes avec un salaire entre 40000 et 60000
        List<Person> midSalaryPersons = Person.query(Person.class)
                .whereBetween("salary", 40000, 60000)
                .execute();
        System.out.println("Mid Salary Persons: " + midSalaryPersons);

        // Récupérer les personnes avec un âge dans une liste spécifique
        List<Person> specificAgePersons = Person.query(Person.class)
                .whereIn("age", Arrays.asList(25, 30))
                .execute();
        System.out.println("Specific Age Persons: " + specificAgePersons);

        // Récupérer les personnes avec un tri par nom de famille
        List<Person> sortedPersons = Person.query(Person.class)
                .orderBy("last_name")
                .execute();
        System.out.println("Sorted Persons: " + sortedPersons);

        // Récupérer les personnes avec un tri descendant par salaire
        List<Person> sortedDescPersons = Person.query(Person.class)
                .orderByDesc("salary")
                .execute();
        System.out.println("Sorted Desc Persons: " + sortedDescPersons);

        // Récupérer les personnes avec une limite de résultats
        List<Person> limitedPersons = Person.query(Person.class)
                .limit(1)
                .execute();
        System.out.println("Limited Persons: " + limitedPersons);

        // Récupérer les personnes avec un offset
        List<Person> offsetPersons = Person.query(Person.class)
                .offset(1)
                .execute();
        System.out.println("Offset Persons: " + offsetPersons);

        // Récupérer les adresses d'une personne spécifique
        List<Address> personAddresses = Address.query(Address.class)
                .where("person_id", Operations.EQUALS, person1.getId())
                .execute();
        System.out.println("Person Addresses: " + personAddresses);

        // Récupérer les adresses avec une jointure sur la table des personnes
        List<Address> addressesWithJoin = Address.query(Address.class)
                .join("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Join: " + addressesWithJoin);

        // Récupérer les adresses avec une jointure gauche sur la table des personnes
        List<Address> addressesWithLeftJoin = Address.query(Address.class)
                .leftJoin("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Left Join: " + addressesWithLeftJoin);

        // Récupérer les adresses avec une jointure droite sur la table des personnes
        List<Address> addressesWithRightJoin = Address.query(Address.class)
                .rightJoin("persons", "addresses.person_id", Operations.EQUALS, "persons.id")
                .execute();
        System.out.println("Addresses with Right Join: " + addressesWithRightJoin);

        // Récupérer les adresses groupées par ville
        List<Address> addressesGroupedByCity = Address.query(Address.class)
                .groupBy("city")
                .execute();
        System.out.println("Addresses Grouped by City: " + addressesGroupedByCity);

        // Récupérer les adresses avec une condition HAVING
        List<Address> addressesHavingCondition = Address.query(Address.class)
                .groupBy("city")
                .having("COUNT(city)", Operations.GREATER_THAN, 1)
                .execute();
        System.out.println("Addresses with HAVING Condition: " + addressesHavingCondition);

        // Supprimer toutes les personnes avec un critère (par exemple, âge supérieur à 30)
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