package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
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
  
  public void crearEstudio(Scanner keyboard) {
    log.info("Into crearEstudio EstudioEntity in Input Adapter");
    try {
      System.out.print("Ingrese la cedula de la persona: ");
      Integer personIdentification = keyboard.nextInt();
      keyboard.nextLine();

      System.out.print("Ingrese el id de la profesion: ");
      Integer professionIdentification = keyboard.nextInt();
      keyboard.nextLine();

      System.out.print("Ingrese la fecha de graduacion (YYYY-MM-DD): ");
      String graduationDateInput = keyboard.nextLine();

      System.out.print("Ingrese el nombre de la universidad: ");
      String universityName = keyboard.nextLine();

      Person person = new Person();
      person.updateIdentification(personIdentification);

      Profession profession = new Profession();
      profession.updateIdentification(professionIdentification);

      Study study = new Study();
      study.updatePerson(person);
      study.updateProfession(profession);
      study.updateGraduationDate(graduationDateInput.isBlank() ? null : LocalDate.parse(graduationDateInput));
      study.updateUniversityName(universityName);

      studyInputPort.create(study);
      System.out.println("Estudio creado exitosamente.");
    } catch (Exception e) {
      log.warn("Error creando estudio: " + e.getMessage());
    }
  }

  public void updateEstudio(Scanner keyboard) {
    log.info("Into updateEstudio EstudioEntity in Input Adapter");
    log.warn("Study update is not supported by the current input port.");
  }

  public void deleteEstudio(Scanner keyboard) {
    log.info("Into deleteEstudio EstudioEntity in Input Adapter");
    log.warn("Study delete is not supported by the current input port.");
  }
}
