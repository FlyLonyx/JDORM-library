package fr.flylonyx.example.models;

import fr.flylonyx.library.annotations.Column;
import fr.flylonyx.library.annotations.Table;
import fr.flylonyx.library.core.Model;
import fr.flylonyx.library.utils.Operations;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Table(name = "persons")
@Getter
@Setter
public class Person extends Model {
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private int age;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "salary")
    private double salary;

    @Column(name = "birth_date")
    private String birthDate;

    @Column(name = "created_at")
    private String createdAt;

    public List<Address> getAddresses() {
        try {
            return Address.query(Address.class)
                    .where("person_id", Operations.EQUALS, id)
                    .execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
