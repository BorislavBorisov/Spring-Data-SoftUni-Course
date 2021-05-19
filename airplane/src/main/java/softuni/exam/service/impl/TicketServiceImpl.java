package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.TicketImportDto;
import softuni.exam.models.dtos.xmls.TicketImportRootDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Plane;
import softuni.exam.models.entities.Ticket;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.repository.TicketRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TicketService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TicketServiceImpl implements TicketService {
    private static final String URL_PATH = "src/main/resources/files/xml/tickets.xml";
    private final TicketRepository ticketRepository;
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public TicketServiceImpl(TicketRepository ticketRepository,
                             PassengerRepository passengerRepository,
                             TownRepository townRepository,
                             PlaneRepository planeRepository,
                             XmlParser xmlParser,
                             ModelMapper modelMapper,
                             ValidationUtil validationUtil) {
        this.ticketRepository = ticketRepository;
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.ticketRepository.count() > 0;
    }

    @Override
    public String readTicketsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(URL_PATH)));
    }

    @Override
    public String importTickets() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        TicketImportRootDto ticketImportRootDto = this.xmlParser.parseXml(TicketImportRootDto.class, URL_PATH);
        for (TicketImportDto ticketImportDto : ticketImportRootDto.getTickets()) {
            Plane planeByRegisterNumber = this.planeRepository.getPlaneByRegisterNumber(ticketImportDto.getPlaneDto().getRegisterNumber());
            Passenger passengerByEmail = this.passengerRepository.getPassengerByEmail(ticketImportDto.getPassengerDto().getEmail());
            Town fromTown = this.townRepository.getTownByName(ticketImportDto.getFromTownDto().getFromTown());
            Town toTown = this.townRepository.getTownByName(ticketImportDto.getToTownDto().getToTown());
            if (this.validationUtil.isValid(ticketImportDto) && planeByRegisterNumber != null && passengerByEmail != null
                    && fromTown != null && toTown != null) {
                Ticket map = this.modelMapper.map(ticketImportDto, Ticket.class);
                map.setPlane(planeByRegisterNumber);
                map.setPassenger(passengerByEmail);
                map.setFromTown(fromTown);
                map.setToTown(toTown);
                sb.append(String.format("Successfully imported Ticket %s - %s", map.getFromTown().getName(),
                        map.getToTown().getName())).append(System.lineSeparator());
                this.ticketRepository.saveAndFlush(map);
            } else {
                sb.append("Invalid ticket").append(System.lineSeparator());
            }
        }
        return sb.toString().trim();
    }
}
