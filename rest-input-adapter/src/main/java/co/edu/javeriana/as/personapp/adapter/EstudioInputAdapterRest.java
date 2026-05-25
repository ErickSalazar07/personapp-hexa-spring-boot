package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mapper.EstudioMapperRest;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterRest {

    @Autowired
    @Qualifier("studyOutputAdapterMaria")
    private StudyOutputPort studyOutputPortMaria;

    @Autowired
    @Qualifier("studyOutputAdapterMongo")
    private StudyOutputPort studyOutputPortMongo;

    @Autowired
    private EstudioMapperRest estudioMapperRest;

    @Autowired
    private StudyInputPort studyInputPort;

    private String setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            studyInputPort.setPersintence(studyOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            studyInputPort.setPersintence(studyOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<EstudioResponse> historial(String database) {
        log.info("Into historial Study in REST Adapter");
        try {
            String db = setStudyOutputPortInjection(database);
            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return studyInputPort.findAll().stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return studyInputPort.findAll().stream()
                        .map(estudioMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public EstudioResponse obtenerEstudio(Integer ccPersona, Integer idProfesion, String database) {
        log.info("Into obtenerEstudio in REST Adapter");
        try {
            setStudyOutputPortInjection(database);
            Study study = studyInputPort.findOne(ccPersona, idProfesion);
            return estudioMapperRest.fromDomainToAdapterRest(study, database);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public EstudioResponse crearEstudio(EstudioRequest request) {
        log.info("Into crearEstudio in REST Adapter");
        try {
            setStudyOutputPortInjection(request.getDatabase());
            Study study = studyInputPort.create(estudioMapperRest.fromAdapterToDomain(request));
            return estudioMapperRest.fromDomainToAdapterRest(study, request.getDatabase());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public EstudioResponse editarEstudio(Integer ccPersona, Integer idProfesion, EstudioRequest request) {
        log.info("Into editarEstudio in REST Adapter");
        try {
            setStudyOutputPortInjection(request.getDatabase());
            Study study = studyInputPort.edit(ccPersona, idProfesion, estudioMapperRest.fromAdapterToDomain(request));
            return estudioMapperRest.fromDomainToAdapterRest(study, request.getDatabase());
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public Boolean eliminarEstudio(Integer ccPersona, Integer idProfesion, String database) {
        log.info("Into eliminarEstudio in REST Adapter");
        try {
            setStudyOutputPortInjection(database);
            return studyInputPort.drop(ccPersona, idProfesion);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}