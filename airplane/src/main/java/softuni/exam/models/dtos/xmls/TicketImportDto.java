package softuni.exam.models.dtos.xmls;

import org.hibernate.validator.constraints.Length;
import softuni.exam.config.LocalDateTimeAdapter;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@XmlRootElement(name = "ticket")
@XmlAccessorType(XmlAccessType.FIELD)
public class TicketImportDto {

    @XmlElement(name = "serial-number")
    private String serialNumber;
    @XmlElement
    private BigDecimal price;
    @XmlElement(name = "take-off")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime takeoff;
    @XmlElement(name = "from-town")
    private FromTownDto fromTownDto;
    @XmlElement(name = "to-town")
    private ToTownDto toTownDto;
    @XmlElement(name = "passenger")
    private PassengerDto passengerDto;
    @XmlElement(name = "plane")
    private PlaneDto planeDto;

    public TicketImportDto() {

    }

    @Length(min = 2)
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    @Min(value = 0)
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDateTime getTakeoff() {
        return takeoff;
    }

    public void setTakeoff(LocalDateTime takeoff) {
        this.takeoff = takeoff;
    }

    public FromTownDto getFromTownDto() {
        return fromTownDto;
    }

    public void setFromTownDto(FromTownDto fromTownDto) {
        this.fromTownDto = fromTownDto;
    }

    public ToTownDto getToTownDto() {
        return toTownDto;
    }

    public void setToTownDto(ToTownDto toTownDto) {
        this.toTownDto = toTownDto;
    }

    public PassengerDto getPassengerDto() {
        return passengerDto;
    }

    public void setPassengerDto(PassengerDto passengerDto) {
        this.passengerDto = passengerDto;
    }

    public PlaneDto getPlaneDto() {
        return planeDto;
    }

    public void setPlaneDto(PlaneDto planeDto) {
        this.planeDto = planeDto;
    }
}
