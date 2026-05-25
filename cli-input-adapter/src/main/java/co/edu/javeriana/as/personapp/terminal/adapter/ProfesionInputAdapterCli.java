package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.ProfessionInputPort;
import co.edu.javeriana.as.personapp.application.port.out.ProfessionOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.ProfessionUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.exceptions.NoExistException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.terminal.mapper.ProfesionMapperCli;
import co.edu.javeriana.as.personapp.terminal.model.ProfesionModelCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class ProfesionInputAdapterCli {
  
	@Autowired
	@Qualifier("professionOutputAdapterMaria")
	private ProfessionOutputPort professionOutputPortMaria;

	@Autowired
	@Qualifier("professionOutputAdapterMongo")
	private ProfessionOutputPort professionOutputPortMongo;

	@Autowired
	private ProfesionMapperCli profesionMapperCli;

  ProfessionInputPort professionInputPort;

	public void setProfessionOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			professionInputPort = new ProfessionUseCase(professionOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

  public void historial() {
    log.info("Into historial ProfesionEntity in Input Adapter");
    List<ProfesionModelCli> profesiones = professionInputPort.findAll()
      .stream()
      .map(profesionMapperCli::fromDomainToAdapterCli)
      .collect(Collectors.toList());

    profesiones.forEach(e -> System.out.println(e.toString()));
  }

  public void crearProfesion(Scanner keyboard) {
    log.info("Into crearProfesion ProfesionEntity in Input Adapter");
    try {
      System.out.print("Ingrese el id: ");
      Integer id = keyboard.nextInt();
      keyboard.nextLine();

      System.out.print("Ingrese el nombre: ");
      String name = keyboard.nextLine();

      System.out.print("Ingrese la descripcion: ");
      String description = keyboard.nextLine();

      Profession profession = new Profession();
      profession.updateIdentification(id);
      profession.updateName(name);
      profession.updateDescription(description);

      professionInputPort.create(profession);
      System.out.println("Profesion creada exitosamente.");
    } catch (Exception e) {
      log.warn("Error creando profesion: " + e.getMessage());
    }
  }

  public void updateProfesion(Scanner keyboard) {
    log.info("Into updateProfesion ProfesionEntity in Input Adapter");

    Integer id = 0;
    String name = "";
    String description = "";

    try {
      System.out.print("Ingrese el id: ");
      id = keyboard.nextInt();
      keyboard.nextLine();

      Profession profession = professionInputPort.findOne(id);

      System.out.print("Ingrese el nombre: ");
      name = keyboard.nextLine();

      System.out.print("Ingrese la descripcion: ");
      description = keyboard.nextLine();

      profession.updateName(name);
      profession.updateDescription(description);

      professionInputPort.edit(id, profession);
      System.out.println("Profesion actualizada exitosamente.");
    } catch (NoExistException e) {
      log.warn("Error la profesion con id " + id + " no se encuentra");
    } catch (Exception e) {
      log.warn("Error actualizando profesion: " + e.getMessage());
    }
  }

  public void deleteProfesion(Scanner keyboard) {
    log.info("Into deleteProfesion ProfesionEntity in Input Adapter");
    Integer id = 0;
    try {
      System.out.print("Ingrese el id: ");
      id = keyboard.nextInt();

      professionInputPort.drop(id);
      System.out.println("Profesion eliminada exitosamente.");
    } catch (NoExistException e) {
      log.warn("Error la profesion con id " + id + " no se encuentra");
    } catch (Exception e) {
      log.warn("Error eliminando profesion: " + e.getMessage());
    }
  }

}
