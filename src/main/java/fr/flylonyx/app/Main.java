package fr.flylonyx.app;

import fr.flylonyx.app.models.Person;
import fr.flylonyx.orm.core.MigrationManager;
import fr.flylonyx.orm.core.Schema;
import fr.flylonyx.orm.database.DatabaseConnection;
import fr.flylonyx.orm.migrations.AddEmailToPersons;
import fr.flylonyx.orm.migrations.CreatePersonsTable;

import java.sql.Connection;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {

        Connection connection = DatabaseConnection.getConnection();
        Schema schema = new Schema(connection);

        MigrationManager.initialize();

        MigrationManager.applyMigration("CreatePersonsTable", () -> {
            try {
                CreatePersonsTable.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        MigrationManager.applyMigration("AddEmailToPersons", () -> {
            try {
                AddEmailToPersons.up(schema);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Exemple 1: Ajouter de nouvelles personnes dans la base de données
        Person person1 = new Person();
        person1.setFirstName("John");
        person1.setLastName("Doe");
        person1.setEmail("john.doe@example.com");
        person1.setAge(30);
        person1.save();

        Person person2 = new Person();
        person2.setFirstName("Jane");
        person2.setLastName("Smith");
        person2.setEmail("jane.smith@example.com");
        person2.setAge(25);
        person2.save();

        Person person3 = new Person();
        person3.setFirstName("Alice");
        person3.setLastName("Johnson");
        person3.setEmail("alice.johnson@example.com");
        person3.setAge(35);
        person3.save();

        // Exemple 2: Trouver une personne par son ID
        Person foundPerson = Person.findById(1, Person.class);
        if (foundPerson != null) {
            System.out.println("Found person by ID: " + foundPerson.getFirstName() + " " + foundPerson.getLastName());
        } else {
            System.out.println("Person not found.");
        }

        // Exemple 3: Modifier les informations d'une personne
        if (foundPerson != null) {
            foundPerson.setAge(31);
            foundPerson.update();
            System.out.println("Updated person: " + foundPerson.getFirstName() + " " + foundPerson.getLastName());
        }

        // Exemple 4: Utiliser la requête 'query' pour récupérer la première personne triée par âge décroissant
        Person oldestPerson = Person.query(Person.class)
                .orderByDesc("age")
                .first();
        if (oldestPerson != null) {
            System.out.println("Oldest person: " + oldestPerson.getFirstName() + " " + oldestPerson.getLastName());
        } else {
            System.out.println("No persons found.");
        }

        // Exemple 5: Utiliser 'query' pour obtenir toutes les personnes avec une limite de 2 résultats
        List<Person> people = Person.query(Person.class)
                .limit(2)
                .execute();
        System.out.println("People (limit 2): ");
        for (Person p : people) {
            System.out.println(p.getFirstName() + " " + p.getLastName());
        }

        // Exemple 6: Supprimer une personne
        if (foundPerson != null) {
            foundPerson.delete();
            System.out.println("Person deleted: " + foundPerson.getFirstName() + " " + foundPerson.getLastName());
        }

        // Exemple 7: Rechercher des personnes avec des critères spécifiques via la méthode QueryBuilder
        List<Person> filteredPeople = Person.query(Person.class)
                .orderByDesc("age")
                .limit(3)
                .execute();

        System.out.println("Filtered people:");
        for (Person p : filteredPeople) {
            System.out.println(p.getFirstName() + " " + p.getLastName() + " - Age: " + p.getAge());
        }

        // Exemple 8: Supprimer toutes les personnes avec un critère (par exemple, âge supérieur à 30)
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
