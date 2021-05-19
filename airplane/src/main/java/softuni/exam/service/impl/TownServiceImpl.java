package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.jsons.TownImportDto;
import softuni.exam.models.entities.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class TownServiceImpl implements TownService {
    private static final String URL_PATH = "src/main/resources/files/json/towns.json";
    private final TownRepository townRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public TownServiceImpl(TownRepository townRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.townRepository = townRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }


    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(URL_PATH)));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();

        TownImportDto[] townImportDto = this.gson.fromJson(readTownsFileContent(), TownImportDto[].class);

        for (TownImportDto importDto : townImportDto) {
            if (this.validationUtil.isValid(importDto)) {
                Town map = this.modelMapper.map(importDto, Town.class);
                sb.append(String.format("Successfully imported Town %s - %d",
                        importDto.getName(), importDto.getPopulation())).append(System.lineSeparator());
                this.townRepository.saveAndFlush(map);
            } else {
                sb.append("Invalid town").append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }
}
