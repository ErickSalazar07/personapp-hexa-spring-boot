package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.TelefonoInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TelefonoMenu {
	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
  private static final int OPCION_CREAR_TELEFONO = 2;
  private static final int OPCION_ACTUALIZAR_TELEFONO = 3;
  private static final int OPCION_ELIMINAR_TELEFONO = 4;
  
	public void iniciarMenu(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuMotorPersistencia();
				int opcion = leerOpcion(keyboard);

				switch (opcion) {

				case OPCION_REGRESAR_MODULOS:
					isValid = true;
				break;

				case PERSISTENCIA_MARIADB:
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MARIA");
					menuOpciones(telefonoInputAdapterCli,keyboard);
				break;

				case PERSISTENCIA_MONGODB:
					telefonoInputAdapterCli.setPhoneOutputPortInjection("MONGO");
					menuOpciones(telefonoInputAdapterCli,keyboard);
				break;

				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(TelefonoInputAdapterCli telefonoInputAdapterCli, Scanner keyboard) {
		boolean isValid = false;
		do {
			try {
				mostrarMenuOpciones();
				int opcion = leerOpcion(keyboard);
				switch (opcion) {

				case OPCION_REGRESAR_MOTOR_PERSISTENCIA:
					isValid = true;
				break;

				case OPCION_VER_TODO:
					telefonoInputAdapterCli.historial();					
				break;

        case OPCION_CREAR_TELEFONO:
          telefonoInputAdapterCli.crearTelefono(keyboard);
        break;

        case OPCION_ACTUALIZAR_TELEFONO:
          telefonoInputAdapterCli.historial();
          telefonoInputAdapterCli.updateTelefono(keyboard);
        break;

        case OPCION_ELIMINAR_TELEFONO:
          telefonoInputAdapterCli.historial();
          telefonoInputAdapterCli.deleteTelefono(keyboard);
        break;

				// mas opciones
				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InputMismatchException e) {
				log.warn("Solo se permiten números.");
			}
		} while (!isValid);
	}

	private void mostrarMenuOpciones() {
		System.out.println("----------------------");
		System.out.println(OPCION_VER_TODO + " para ver todos los telefonos");
		System.out.println(OPCION_CREAR_TELEFONO + " para crear un telefono");
		System.out.println(OPCION_ACTUALIZAR_TELEFONO + " para actualizar un telefono");
		System.out.println(OPCION_ELIMINAR_TELEFONO + " para eliminar un telefono");
		// implementar otras opciones
		System.out.println(OPCION_REGRESAR_MOTOR_PERSISTENCIA + " para regresar");
	}

	private void mostrarMenuMotorPersistencia() {
		System.out.println("----------------------");
		System.out.println(PERSISTENCIA_MARIADB + " para MariaDB");
		System.out.println(PERSISTENCIA_MONGODB + " para MongoDB");
		System.out.println(OPCION_REGRESAR_MODULOS + " para regresar");
	}

	private int leerOpcion(Scanner keyboard) {
    int opc = 0;
		try {
			System.out.print("Ingrese una opción: ");
			opc = keyboard.nextInt();
      keyboard.nextLine();
      return opc;
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
      keyboard.nextLine();
			return leerOpcion(keyboard);
		}
	}
}
