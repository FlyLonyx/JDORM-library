package fr.flylonyx.example.models;

import fr.flylonyx.library.annotations.Column;
import fr.flylonyx.library.annotations.Table;
import fr.flylonyx.library.core.Model;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

@Table(name = "addresses")
@Getter
@Setter
public class Address extends Model {
    @Column(name = "id")
    private int id;

    @Column(name = "street")
    private String street;

    @Column(name = "city")
    private String city;

    @Column(name = "person_id")
    private int personId;

    public Person getPerson() throws SQLException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
            return Person.findById(personId, Person.class);
    }
}
