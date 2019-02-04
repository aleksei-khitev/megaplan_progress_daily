package ru.akhitev.megaplan.progress.daily.entity;

import javax.persistence.*;

@Entity
@Table(name = "employee")
@SequenceGenerator(name = "seq", initialValue = 20)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    private int id;

    @Column(name = "name")
    private String name;

    public void validateForSave() {
        if (name == null || name.length()==0) {
            throw new IllegalArgumentException("Не задано имя работника", null);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
