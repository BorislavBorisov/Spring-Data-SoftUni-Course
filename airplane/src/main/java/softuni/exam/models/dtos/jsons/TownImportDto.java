package softuni.exam.models.dtos.jsons;

import com.google.gson.annotations.Expose;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;

public class TownImportDto {
    @Expose
    private String name;
    @Expose
    private int population;
    @Expose
    private String guide;

    public TownImportDto() {

    }

    @Length(min = 2)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Min(value = 0)
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getGuide() {
        return guide;
    }

    public void setGuide(String guide) {
        this.guide = guide;
    }
}
