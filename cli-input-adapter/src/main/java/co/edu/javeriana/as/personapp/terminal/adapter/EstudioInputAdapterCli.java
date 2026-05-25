package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.terminal.mapper.EstudioMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class EstudioInputAdapterCli {

  @Autowired
  @Qualifier("studyOutputAdapterMaria")
  private StudyOutputPort studyOutputPortMaria;

  @Autowired
  @Qualifier("studyOutputAdapterMongo")
  private StudyOutputPort studyOutputPortMongo;
  
  @Autowired
  private EstudioMapperCli estudioMapperCli;

  StudyInputPort studyInputPort;
  
  public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
  }
  
  public void historial() {
    log.info("Into historial EstudioEntity in Input Adapter");
    List<EstudioModelCli> estudios = studyInputPort.findAll()
      .stream()
      .map(estudioMapperCli::fromDomainToAdapterCli)
      .collect(Collectors.toList());

    estudios.forEach(e -> System.out.println(e.toString()));
  }
  
}
