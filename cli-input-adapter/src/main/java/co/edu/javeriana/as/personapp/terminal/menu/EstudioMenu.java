package co.edu.javeriana.as.personapp.terminal.menu;

import java.util.InputMismatchException;
import java.util.Scanner;

import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.terminal.adapter.EstudioInputAdapterCli;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EstudioMenu {
  
	private static final int OPCION_REGRESAR_MODULOS = 0;
	private static final int PERSISTENCIA_MARIADB = 1;
	private static final int PERSISTENCIA_MONGODB = 2;

	private static final int OPCION_REGRESAR_MOTOR_PERSISTENCIA = 0;
	private static final int OPCION_VER_TODO = 1;
  private static final int OPCION_CREAR_ESTUDIO = 2;
  private static final int OPCION_ACTUALIZAR_ESTUDIO = 3;
  private static final int OPCION_ELIMINAR_ESTUDIO = 4;
  
	public void iniciarMenu(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
					estudioInputAdapterCli.setStudyOutputPortInjection("MARIA");
					menuOpciones(estudioInputAdapterCli,keyboard);
				break;

				case PERSISTENCIA_MONGODB:
					estudioInputAdapterCli.setStudyOutputPortInjection("MONGO");
					menuOpciones(estudioInputAdapterCli,keyboard);
				break;

				default:
					log.warn("La opción elegida no es válida.");
				}
			} catch (InvalidOptionException e) {
				log.warn(e.getMessage());
			}
		} while (!isValid);
	}

	private void menuOpciones(EstudioInputAdapterCli estudioInputAdapterCli, Scanner keyboard) {
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
					estudioInputAdapterCli.historial();
				break;

        case OPCION_CREAR_ESTUDIO:
          estudioInputAdapterCli.crearEstudio(keyboard);
        break;

        case OPCION_ACTUALIZAR_ESTUDIO:
          estudioInputAdapterCli.historial();
          estudioInputAdapterCli.updateEstudio(keyboard);
        break;

        case OPCION_ELIMINAR_ESTUDIO:
          estudioInputAdapterCli.historial();
          estudioInputAdapterCli.deleteEstudio(keyboard);
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
		System.out.println(OPCION_CREAR_ESTUDIO + " para crear una profesiones");
		System.out.println(OPCION_ACTUALIZAR_ESTUDIO + " para actualizar una profesiones");
		System.out.println(OPCION_ELIMINAR_ESTUDIO + " para eliminar una profesiones");
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
		try {
			System.out.print("Ingrese una opción: ");
			return keyboard.nextInt();
		} catch (InputMismatchException e) {
			log.warn("Solo se permiten números.");
      keyboard.nextLine();
			return leerOpcion(keyboard);
		}
	}
}
