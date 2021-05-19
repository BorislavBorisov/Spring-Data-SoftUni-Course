package softuni.exam.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Town;

import java.util.Optional;

@Repository
public interface TownRepository extends JpaRepository<Town, Integer> {

    @Query("SELECT t FROM Town  as t WHERE t.name=:name")
    Town getTownByName(String name);



}
