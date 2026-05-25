package co.edu.javeriana.as.personapp.mariadb.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mariadb.entity.PersonaEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.TelefonoEntity;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMaria {

	public TelefonoEntity fromDomainToAdapter(Phone phone) {
		TelefonoEntity telefonoEntity = new TelefonoEntity();

		telefonoEntity.setNum(phone.getNumber());
		telefonoEntity.setOper(phone.getCompany());
		telefonoEntity.setDuenio(validateDuenio(phone.getOwner()));

		return telefonoEntity;
	}

	private PersonaEntity validateDuenio(@NonNull Person owner) {

    PersonaEntity personaEntity = new PersonaEntity();
    personaEntity.setCc(owner.getIdentification());

		return personaEntity;
	}

	public Phone fromAdapterToDomain(TelefonoEntity telefonoEntity) {
		Phone phone = new Phone();

		phone.updateNumber(telefonoEntity.getNum());
		phone.updateCompany(telefonoEntity.getOper());
		phone.updateOwner(validateOwner(telefonoEntity.getDuenio()));

		return phone;
	}

	private @NonNull Person validateOwner(PersonaEntity duenio) {
  	Person person = new Person();

  	person.updateIdentification(duenio.getCc());

  	return person;
  }
}