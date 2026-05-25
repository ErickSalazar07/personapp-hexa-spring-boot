package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.ProfesionInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProfesionMenu {
	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
  private static final int OPCION_CREAR_PROFESION = 2;
  private static final int OPCION_ACTUALIZAR_PROFESION = 3;
  private static final int OPCION_ELIMINAR_PROFESION = 4;
  
	public void iniciarMenu(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
					profesionInputAdapterCli.setProfessionOutputPortInjection("MARIA");
					menuOpciones(profesionInputAdapterCli,keyboard);
				break;

				case PERSISTENCIA_MONGODB:
					profesionInputAdapterCli.setProfessionOutputPortInjection("MONGO");
					menuOpciones(profesionInputAdapterCli,keyboard);
				break;

				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(ProfesionInputAdapterCli profesionInputAdapterCli, Scanner keyboard) {
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
					profesionInputAdapterCli.historial();					
				break;

        case OPCION_CREAR_PROFESION:
          profesionInputAdapterCli.crearProfesion(keyboard);
        break;

        case OPCION_ACTUALIZAR_PROFESION:
          profesionInputAdapterCli.historial();
          profesionInputAdapterCli.updateProfesion(keyboard);
        break;

        case OPCION_ELIMINAR_PROFESION:
          profesionInputAdapterCli.historial();
          profesionInputAdapterCli.deleteProfesion(keyboard);
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
		System.out.println(OPCION_VER_TODO + " para ver todas las profesiones");
		System.out.println(OPCION_CREAR_PROFESION + " para crear una profesion");
		System.out.println(OPCION_ACTUALIZAR_PROFESION + " para actualizar una profesion");
		System.out.println(OPCION_ELIMINAR_PROFESION + " para eliminar una profesion");
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
