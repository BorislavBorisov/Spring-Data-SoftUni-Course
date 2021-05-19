package softuni.exam.models.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "towns")
public class Town extends BaseEntity {

    private String name;
    private int population;
    private String guide;
    private Set<Passenger> passengers;
    private Set<Ticket> fromTown;
    private Set<Ticket> toTown;


    public Town() {

    }

    @Column(unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Column
    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }

    @OneToMany(mappedBy = "town")
    public Set<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<Passenger> passengers) {
        this.passengers = passengers;
    }

    @OneToMany(mappedBy = "fromTown")
    public Set<Ticket> getFromTown() {
        return fromTown;
    }

    public void setFromTown(Set<Ticket> fromTown) {
        this.fromTown = fromTown;
    }

    @OneToMany(mappedBy = "toTown")
    public Set<Ticket> getToTown() {
        return toTown;
    }

    public void setToTown(Set<Ticket> toTown) {
        this.toTown = toTown;
    }
}
