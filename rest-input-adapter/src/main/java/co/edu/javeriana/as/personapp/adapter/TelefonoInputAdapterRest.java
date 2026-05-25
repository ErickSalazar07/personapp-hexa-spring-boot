package co.edu.javeriana.as.personapp.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mapper.TelefonoMapperRest;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterRest {

    @Autowired
    @Qualifier("phoneOutputAdapterMaria")
    private PhoneOutputPort phoneOutputPortMaria;

    @Autowired
    @Qualifier("phoneOutputAdapterMongo")
    private PhoneOutputPort phoneOutputPortMongo;

    @Autowired
    private TelefonoMapperRest telefonoMapperRest;

    @Autowired
    private PhoneInputPort phoneInputPort;

    private String setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
        if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
            phoneInputPort.setPersintence(phoneOutputPortMaria);
            return DatabaseOption.MARIA.toString();
        } else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
            phoneInputPort.setPersintence(phoneOutputPortMongo);
            return DatabaseOption.MONGO.toString();
        } else {
            throw new InvalidOptionException("Invalid database option: " + dbOption);
        }
    }

    public List<TelefonoResponse> historial(String database) {
        log.info("Into historial Phone in REST Adapter");
        try {
            String db = setPhoneOutputPortInjection(database);
            if (db.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
                return phoneInputPort.findAll().stream()
                        .map(telefonoMapperRest::fromDomainToAdapterRestMaria)
                        .collect(Collectors.toList());
            } else {
                return phoneInputPort.findAll().stream()
                        .map(telefonoMapperRest::fromDomainToAdapterRestMongo)
                        .collect(Collectors.toList());
            }
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return new ArrayList<>();
        }
    }

    public TelefonoResponse obtenerTelefono(String numero, String database) {
        log.info("Into obtenerTelefono in REST Adapter");
        try {
            setPhoneOutputPortInjection(database);
            Phone phone = phoneInputPort.findOne(numero);
            return telefonoMapperRest.fromDomainToAdapterRest(phone, database);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public TelefonoResponse crearTelefono(TelefonoRequest request) {
        log.info("Into crearTelefono in REST Adapter");
        try {
            setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneInputPort.create(telefonoMapperRest.fromAdapterToDomain(request));
            return telefonoMapperRest.fromDomainToAdapterRest(phone, request.getDatabase());
        } catch (InvalidOptionException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public TelefonoResponse editarTelefono(String numero, TelefonoRequest request) {
        log.info("Into editarTelefono in REST Adapter");
        try {
            setPhoneOutputPortInjection(request.getDatabase());
            Phone phone = phoneInputPort.edit(numero, telefonoMapperRest.fromAdapterToDomain(request));
            return telefonoMapperRest.fromDomainToAdapterRest(phone, request.getDatabase());
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return null;
        }
    }

    public Boolean eliminarTelefono(String numero, String database) {
        log.info("Into eliminarTelefono in REST Adapter");
        try {
            setPhoneOutputPortInjection(database);
            return phoneInputPort.drop(numero);
        } catch (InvalidOptionException | NoExistException e) {
            log.warn(e.getMessage());
            return false;
        }
    }
}