package co.edu.javeriana.as.personapp.terminal.adapter;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import co.edu.javeriana.as.personapp.application.port.in.PhoneInputPort;
import co.edu.javeriana.as.personapp.application.port.out.PhoneOutputPort;
import co.edu.javeriana.as.personapp.application.usecase.PhoneUseCase;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.common.setup.DatabaseOption;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.mapper.TelefonoMapperCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter
public class TelefonoInputAdapterCli {
  
	@Autowired
	@Qualifier("phoneOutputAdapterMaria")
	private PhoneOutputPort phoneOutputPortMaria;

	@Autowired
	@Qualifier("phoneOutputAdapterMongo")
	private PhoneOutputPort phoneOutputPortMongo;

	@Autowired
	private TelefonoMapperCli telefonoMapperCli;

	PhoneInputPort phoneInputPort;

	public void setPhoneOutputPortInjection(String dbOption) throws InvalidOptionException {
		if (dbOption.equalsIgnoreCase(DatabaseOption.MARIA.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMaria);
		} else if (dbOption.equalsIgnoreCase(DatabaseOption.MONGO.toString())) {
			phoneInputPort = new PhoneUseCase(phoneOutputPortMongo);
		} else {
			throw new InvalidOptionException("Invalid database option: " + dbOption);
		}
	}

	public void historial() {
    log.info("Into historial TelefonoEntity in Input Adapter");
    phoneInputPort.findAll().stream()
      .map(telefonoMapperCli::fromDomainToAdapterCli)
      .forEach(System.out::println);
	}

  public void crearTelefono(Scanner keyboard) {
    log.info("Into crearTelefono TelefonoEntity in Input Adapter");
    try {
      System.out.print("Ingrese el numero: ");
      String number = keyboard.nextLine();

      System.out.print("Ingrese la compania: ");
      String company = keyboard.nextLine();

      System.out.print("Ingrese la cedula del dueno: ");
      Integer ownerIdentification = keyboard.nextInt();
      keyboard.nextLine();

      Person owner = new Person();
      owner.updateIdentification(ownerIdentification);

      Phone phone = new Phone();
      phone.updateNumber(number);
      phone.updateCompany(company);
      phone.updateOwner(owner);

      phoneInputPort.create(phone);
      System.out.println("Telefono creado exitosamente.");
    } catch (Exception e) {
      log.warn("Error creando telefono: " + e.getMessage());
    }
  }

  public void updateTelefono(Scanner keyboard) {
    log.info("Into updateTelefono TelefonoEntity in Input Adapter");
    try {
      System.out.print("Ingrese el numero actual: ");
      String number = keyboard.nextLine();

      Phone phone = phoneInputPort.findOne(number);

      System.out.print("Ingrese el nuevo numero: ");
      String newNumber = keyboard.nextLine();

      System.out.print("Ingrese la compania: ");
      String company = keyboard.nextLine();

      System.out.print("Ingrese la cedula del dueno: ");
      Integer ownerIdentification = keyboard.nextInt();
      keyboard.nextLine();

      Person owner = new Person();
      owner.updateIdentification(ownerIdentification);

      phone.updateNumber(newNumber);
      phone.updateCompany(company);
      phone.updateOwner(owner);

      phoneInputPort.edit(number, phone);
      System.out.println("Telefono actualizado exitosamente.");
    } catch (Exception e) {
      log.warn("Error actualizando telefono: " + e.getMessage());
    }
  }

  public void deleteTelefono(Scanner keyboard) {
    log.info("Into deleteTelefono TelefonoEntity in Input Adapter");
    try {
      System.out.print("Ingrese el numero: ");
      String number = keyboard.nextLine();

      phoneInputPort.drop(number);
      System.out.println("Telefono eliminado exitosamente.");
    } catch (Exception e) {
      log.warn("Error eliminando telefono: " + e.getMessage());
    }
  }
}
