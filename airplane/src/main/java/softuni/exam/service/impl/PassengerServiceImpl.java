package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.PassengerImportDto;
import softuni.exam.models.entities.Passenger;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.PassengerRepository;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.PassengerService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Service
public class PassengerServiceImpl implements PassengerService {
    private static final String URL_PATH = "src/main/resources/files/json/passengers.json";
    private final PassengerRepository passengerRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public PassengerServiceImpl(PassengerRepository passengerRepository, TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.passengerRepository = passengerRepository;
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.passengerRepository.count() > 0;
    }

    @Override
    public String readPassengersFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(URL_PATH)));
    }

    @Override
    public String importPassengers() throws IOException {
        StringBuilder sb = new StringBuilder();

        PassengerImportDto[] passengerImportDtos = this.gson.fromJson(readPassengersFileContent(), PassengerImportDto[].class);

        for (PassengerImportDto passengerImportDto : passengerImportDtos) {
            Town townByName = this.townRepository.getTownByName(passengerImportDto.getTown());
            if (this.validationUtil.isValid(passengerImportDto) && townByName != null) {
                Passenger map = this.modelMapper.map(passengerImportDto, Passenger.class);
                map.setTown(townByName);
                sb.append(String.format("Successfully imported Passenger %s - %s",
                        map.getLastName(), map.getEmail())).append(System.lineSeparator());
                this.passengerRepository.saveAndFlush(map);
            } else {
                sb.append("Invalid passenger").append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }

    @Override
    public String getPassengersOrderByTicketsCountDescendingThenByEmail() {
        StringBuilder sb = new StringBuilder();
        List<Passenger> allByAndSortByCountOfTicket = this.passengerRepository.getAllByAndSortByCountOfTicket();

        for (Passenger passenger : allByAndSortByCountOfTicket) {
            sb.append(String.format("Passenger - %s  %s", passenger.getFirstName(), passenger.getLastName())).append(System.lineSeparator());
            sb.append(String.format("   Email -  %s  ", passenger.getEmail())).append(System.lineSeparator());
            sb.append(String.format("   Phone -  %s  ", passenger.getPhoneNumber())).append(System.lineSeparator());
            sb.append(String.format("   Number of tickets -   %d  ", passenger.getTickets().size())).append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
//    Passenger {firstName}  {lastName}
//        Email - {email}
//        Phone - {phoneNumber}
//        Number of tickets - {number of tickets}
