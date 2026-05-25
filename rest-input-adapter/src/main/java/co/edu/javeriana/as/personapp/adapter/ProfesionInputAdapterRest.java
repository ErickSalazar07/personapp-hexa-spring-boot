package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.mapper.ProfesionMapperRest;
import co.edu.javeriana.as.personapp.model.request.ProfesionRequest;
import co.edu.javeriana.as.personapp.model.response.ProfesionResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterRest {

    @Autowired
    @Qualifier("professionOutputAdapterMaria")
    private ProfessionOutputPort professionOutputPortMaria;

    @Autowired
    @Qualifier("professionOutputAdapterMongo")
    private ProfessionOutputPort professionOutputPortMongo;

    @Autowired
    private ProfesionMapperRest profesionMapperRest;

    @Autowired
    private ProfessionInputPort professionInputPort;

    private String setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            professionInputPort.setPersintence(professionOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            professionInputPort.setPersintence(professionOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<ProfesionResponse> historial(String database) {
        log.info("Into historial Profession in REST Adapter");
        try {
            String db = setProfessionOutputPortInjection(database);
            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return professionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return professionInputPort.findAll().stream()
                        .map(profesionMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public ProfesionResponse obtenerProfesion(Integer id, String database) {
        log.info("Into obtenerProfesion in REST Adapter");
        try {
            setProfessionOutputPortInjection(database);
            Profession profession = professionInputPort.findOne(id);
            return profesionMapperRest.fromDomainToAdapterRest(profession, database);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfesionResponse crearProfesion(ProfesionRequest request) {
        log.info("Into crearProfesion in REST Adapter");
        try {
            setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.create(profesionMapperRest.fromAdapterToDomain(request));
            return profesionMapperRest.fromDomainToAdapterRest(profession, request.getDatabase());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public ProfesionResponse editarProfesion(Integer id, ProfesionRequest request) {
        log.info("Into editarProfesion in REST Adapter");
        try {
            setProfessionOutputPortInjection(request.getDatabase());
            Profession profession = professionInputPort.edit(id, profesionMapperRest.fromAdapterToDomain(request));
            return profesionMapperRest.fromDomainToAdapterRest(profession, request.getDatabase());
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public Boolean eliminarProfesion(Integer id, String database) {
        log.info("Into eliminarProfesion in REST Adapter");
        try {
            setProfessionOutputPortInjection(database);
            return professionInputPort.drop(id);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}