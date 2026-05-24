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
@ToString
public class Person {

  @NonNull
  private Integer identification;
  @NonNull
  private String firstName;
  @NonNull
  private String lastName;
  @NonNull
  private Gender gender;
  private Integer age;

  @ToString.Exclude
  private List<Phone> phoneNumbers;
  @ToString.Exclude
  private List<Study> studies;

  public void updateIdentification(Integer identification) {
    if (identification == null || identification <= 0)
      throw new IllegalArgumentException("La identificación debe ser mayor a 0");

    this.identification = identification;
  }

  public void updateFirstName(String firstName) {
    if (firstName == null || firstName.isBlank() || !firstName.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"))
      throw new IllegalArgumentException("El nombre no puede contener números ni estar vacío");

    this.firstName = firstName;
  }

  public void updateLastName(String lastName) {
    if (lastName == null || lastName.isBlank() || !lastName.matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"))
      throw new IllegalArgumentException("El apellido no puede contener números ni estar vacío");

    this.lastName = lastName;
  }

  public void updateGender(Gender gender) {
    if (gender == null)
      throw new IllegalArgumentException("El género no puede ser nulo");

    this.gender = gender;
  }

  public void updateAge(Integer age) {
    if (age == null || age < 0)
      throw new IllegalArgumentException("La edad no puede ser nula y  debe ser mayor o igual a 0");

    this.age = age;
  }

  public void updatePhoneNumbers(List<Phone> phoneNumbers) {
    if (phoneNumbers == null)
      throw new IllegalArgumentException("los numeros de telefonos no pueden ser nulos");

    this.phoneNumbers = phoneNumbers;
  }

  public void updateStudies(List<Study> studies) {
    if (studies == null)
      throw new IllegalArgumentException("los estudios no pueden ser nulos");

    this.studies = studies;
  }

  public Boolean isValidAge() {
    return this.age >= 0;
  }
}