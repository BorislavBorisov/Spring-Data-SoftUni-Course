package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.PlaneImportDto;
import softuni.exam.models.dtos.xmls.PlaneImportRootDto;
import softuni.exam.models.entities.Plane;
import softuni.exam.repository.PlaneRepository;
import softuni.exam.service.PlaneService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PlaneServiceImpl implements PlaneService {
    private static final String URL_PATH = "src/main/resources/files/xml/planes.xml";
    private final PlaneRepository planeRepository;
    private final XmlParser xmlParser;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    public PlaneServiceImpl(PlaneRepository planeRepository, XmlParser xmlParser, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.planeRepository = planeRepository;
        this.xmlParser = xmlParser;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public boolean areImported() {
        return this.planeRepository.count() > 0;
    }

    @Override
    public String readPlanesFileContent() throws IOException {
        return String.join("", Files.readAllLines(Path.of(URL_PATH)));
    }

    @Override
    public String importPlanes() throws JAXBException {
        StringBuilder sb = new StringBuilder();

        PlaneImportRootDto planeImportRootDto = this.xmlParser.parseXml(PlaneImportRootDto.class, URL_PATH);
        for (PlaneImportDto planeImportDto : planeImportRootDto.getPlanes()) {
            if (this.validationUtil.isValid(planeImportDto)) {
                Plane map = this.modelMapper.map(planeImportDto, Plane.class);
                sb.append(String.format("Successfully imported Plane %s", map.getRegisterNumber())).append(System.lineSeparator());
                this.planeRepository.saveAndFlush(map);
            } else {
                sb.append("Invalid plane").append(System.lineSeparator());
            }
        }

        return sb.toString().trim();
    }
}
