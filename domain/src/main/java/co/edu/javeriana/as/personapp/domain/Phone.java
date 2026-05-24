package co.edu.javeriana.as.personapp.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
  @NonNull
  private String number;

  @NonNull
  private String company;

  @NonNull
  private Person owner;

  public void updateNumber(String number) {
    if (number == null || number.isBlank())
      throw new IllegalArgumentException("El número no puede estar vacío");
    if (!number.matches("(\\+? ?\\d{0,2} ?)?\\d{3,}"))
      throw new IllegalArgumentException("El número no tiene un formato válido");

    this.number = number;
  }

  public void updateCompany(String company) {
    if (company == null || company.isBlank())
      throw new IllegalArgumentException("La compañía no puede estar vacía");

    this.company = company;
  }

  public void updateOwner(Person owner) {
    if (owner == null)
      throw new IllegalArgumentException("El dueño no puede ser nulo");

    this.owner = owner;
  }

}
