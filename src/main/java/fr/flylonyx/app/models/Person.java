package fr.flylonyx.app.models;


import fr.flylonyx.orm.annotations.Column;
import fr.flylonyx.orm.annotations.Table;
import fr.flylonyx.orm.core.Model;
import lombok.Getter;
import lombok.Setter;

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
}
