package co.edu.javeriana.as.personapp.domain;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Study {
  @NonNull
  private Person person;

  @NonNull
  private Profession profession;

  private LocalDate graduationDate;
  private String universityName;

  public void updatePerson(Person person) {
    if (person == null)
      throw new IllegalArgumentException("La persona no puede ser nula");

    this.person = person;
  }

  public void updateProfession(Profession profession) {
    if (profession == null)
      throw new IllegalArgumentException("La profesión no puede ser nula");

    this.profession = profession;
  }

  public void updateGraduationDate(LocalDate graduationDate) {
    if (graduationDate != null && graduationDate.isAfter(LocalDate.now()))
      throw new IllegalArgumentException("La fecha de graduación no puede ser futura");

    this.graduationDate = graduationDate;
  }

  public void updateUniversityName(String universityName) {
    if (universityName == null || universityName.isBlank())
      throw new IllegalArgumentException("El nombre de la universidad no puede estar vacío");

    this.universityName = universityName;
  }
}
