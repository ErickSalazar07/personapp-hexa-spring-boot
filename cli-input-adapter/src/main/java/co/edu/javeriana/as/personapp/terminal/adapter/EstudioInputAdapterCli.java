package co.edu.javeriana.as.personapp.terminal.adapter;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.application.usecase.StudyUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
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
  @Qualifier("personOutputAdapterMaria")
  private PersonOutputPort personOutputPortMaria;

  @Autowired
  @Qualifier("personOutputAdapterMongo")
  private PersonOutputPort personOutputPortMongo;

  @Autowired
  @Qualifier("professionOutputAdapterMaria")
  private ProfessionOutputPort professionOutputPortMaria;

  @Autowired
  @Qualifier("professionOutputAdapterMongo")
  private ProfessionOutputPort professionOutputPortMongo;
  
  @Autowired
  private EstudioMapperCli estudioMapperCli;

  StudyInputPort studyInputPort;
  PersonInputPort personInputPort;
  ProfessionInputPort professionInputPort;
  
  public void setStudyOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMaria);
      personInputPort = new PersonUseCase(personOutputPortMaria);
      professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			studyInputPort = new StudyUseCase(studyOutputPortMongo);
      personInputPort = new PersonUseCase(personOutputPortMongo);
      professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
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

    Integer personIdentification = 0;
    Integer professionIdentification = 0;
    String graduationDateInput = "";
    String universityName = "";
    Person person = null;
    Profession profession = null;

    try {

      System.out.print("Ingrese la cedula de la persona: ");
      personIdentification = keyboard.nextInt();
      keyboard.nextLine();

      System.out.print("Ingrese el id de la profesion: ");
      professionIdentification = keyboard.nextInt();
      keyboard.nextLine();

      System.out.print("Ingrese la fecha de graduacion (YYYY-MM-DD): ");
      graduationDateInput = keyboard.nextLine();

      System.out.print("Ingrese el nombre de la universidad: ");
      universityName = keyboard.nextLine();

      person = personInputPort.findOne(personIdentification);
      profession = professionInputPort.findOne(professionIdentification);

      Study study = new Study();
      study.updatePerson(person);
      study.updateProfession(profession);
      study.updateGraduationDate(graduationDateInput.isBlank() ? null : LocalDate.parse(graduationDateInput));
      study.updateUniversityName(universityName);

      studyInputPort.create(study);
      System.out.println("Estudio creado exitosamente.");
    } catch (NoExistException e) {
      log.warn("Error no existe: " + e.getMessage());
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
