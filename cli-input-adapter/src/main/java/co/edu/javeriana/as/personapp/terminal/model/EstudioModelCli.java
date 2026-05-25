package co.edu.javeriana.as.personapp.terminal.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudioModelCli {
  private Integer ccPersona;
  private Integer idProfesion;
  private LocalDate fechaGraduacion;
  private String nombreUniversidad;
}
