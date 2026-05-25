package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PersonInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PersonOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PersonUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.terminal.mapper.PersonaMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.PersonaModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class PersonaInputAdapterCli {
	@Autowired
	@Qualifier("personOutputAdapterMaria")
	private PersonOutputPort personOutputPortMaria;

	@Autowired
	@Qualifier("personOutputAdapterMongo")
	private PersonOutputPort personOutputPortMongo;

	@Autowired
	private PersonaMapperCli personaMapperCli;

	PersonInputPort personInputPort;

	public void setPersonOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			personInputPort = new PersonUseCase(personOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial1() {
		log.info("Into historial PersonaEntity in Input Adapter");
		List<PersonaModelCli> persona = personInputPort.findAll().stream().map(personaMapperCli::fromDomainToAdapterCli)
					.collect(Collectors.toList());
		persona.forEach(p -> System.out.println(p.toString()));
	}

	public void historial() {
    log.info("Into historial PersonaEntity in Input Adapter");
    personInputPort.findAll().stream()
      .map(personaMapperCli::fromDomainToAdapterCli)
      .forEach(System.out::println);
	}

  public void crearPersona(Scanner keyboard) {
    log.info("Into crearPersona PersonaEntity in Input Adapter");
    try {
      System.out.print("Ingrese la cédula: ");
      Integer cc = keyboard.nextInt();
      keyboard.nextLine(); // limpiar buffer

      System.out.print("Ingrese el nombre: ");
      String nombre = keyboard.nextLine();

      System.out.print("Ingrese el apellido: ");
      String apellido = keyboard.nextLine();

      System.out.print("Ingrese el género (M/F): ");
      String genero = keyboard.nextLine().toUpperCase();

      System.out.print("Ingrese la edad: ");
      Integer edad = keyboard.nextInt();

      Person persona = new Person();
      
      persona.updateIdentification(cc);
      persona.updateFirstName(nombre);
      persona.updateLastName(apellido);
      persona.updateGender(genero.contains("M") ? Gender.MALE : Gender.FEMALE);
      persona.updateAge(edad);
      persona.updatePhoneNumbers(new ArrayList<>());
      persona.updateStudies(new ArrayList<>());

      personInputPort.create(persona);
      System.out.println("Persona creada exitosamente.");
    } catch (Exception e) {
      log.warn("Error creando persona: " + e.getMessage());
    }
  }

  public void updatePersona(Scanner keyboard) {
    log.info("Into updatePersona PersonaEntity in Input Adapter");

    try {
      System.out.print("Ingrese la cédula: ");
      Integer cc = keyboard.nextInt();
      keyboard.nextLine(); // limpiar buffer

      Person persona = personInputPort.findOne(cc);

      System.out.print("Ingrese el nombre: ");
      String nombre = keyboard.nextLine();

      System.out.print("Ingrese el apellido: ");
      String apellido = keyboard.nextLine();

      System.out.print("Ingrese el género (M/F): ");
      String genero = keyboard.nextLine().toUpperCase();

      System.out.print("Ingrese la edad: ");
      Integer edad = keyboard.nextInt();

      persona.updateFirstName(nombre);
      persona.updateLastName(apellido);
      persona.updateGender(genero.contains("M") ? Gender.MALE : Gender.FEMALE);
      persona.updateAge(edad);

      personInputPort.edit(cc,persona);
      System.out.println("Persona actualizada exitosamente.");
    } catch (Exception e) {
      log.warn("Error actualizando persona: " + e.getMessage());
    }
  }

  public void deletePersona(Scanner keyboard) {
    log.info("Into deletePersona PersonaEntity in Input Adapter");

    try {
      System.out.print("Ingrese la cédula: ");
      Integer cc = keyboard.nextInt();
      keyboard.nextLine(); // limpiar buffer

      personInputPort.drop(cc);
      System.out.println("Persona eliminada exitosamente.");
    } catch (Exception e) {
      log.warn("Error eliminada persona: " + e.getMessage());
    }
  }

}
