package co.edu.javeriana.as.personapp.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Profession {
  @NonNull
  private Integer identification;

  @NonNull
  private String name;
  private String description;

  @ToString.Exclude
  private List<Study> studies;

  public void updateIdentification(Integer identification) {
    if (identification == null || identification <= 0)
      throw new IllegalArgumentException("El identificador debe ser mayor a 0");

    this.identification = identification;
  }

  public void updateName(String name) {
    if (name == null || name.isBlank() || !name.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"))
      throw new IllegalArgumentException("El nombre no puede contener números ni estar vacío");

    this.name = name;
  }

  public void updateDescription(String description) {
    if (description == null || description.isBlank())
      throw new IllegalArgumentException("La descripción no puede estar vacía");

    this.description = description;
  }

  public void updateStudies(List<Study> studies) {
    if (studies == null)
      throw new IllegalArgumentException("los estudios no pueden ser nulos");

    this.studies = studies;
  }
}
