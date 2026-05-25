package co.edu.javeriana.as.personapp.mongo.mapper;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.TelefonoDocument;
import lombok.NonNull;

@Mapper
public class TelefonoMapperMongo {
	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	public TelefonoDocument fromDomainToAdapter(Phone phone) {
		TelefonoDocument telefonoDocument = new TelefonoDocument();
		telefonoDocument.setId(phone.getNumber());
		telefonoDocument.setOper(phone.getCompany());
		telefonoDocument.setPrimaryDuenio(validateDuenio(phone.getOwner()));
		return telefonoDocument;
	}

	private PersonaDocument validateDuenio(@NonNull Person owner) {
    PersonaDocument persona = new PersonaDocument();
    persona.setId(owner.getIdentification());
    persona.setNombre(owner.getFirstName());
		return persona;
	}

	public Phone fromAdapterToDomain(TelefonoDocument telefonoDocument) {
		Phone phone = new Phone();
		phone.updateNumber(telefonoDocument.getId());
		phone.updateCompany(telefonoDocument.getOper());
		phone.updateOwner(validateOwner(telefonoDocument.getPrimaryDuenio()));
		return phone;
	}

	private @NonNull Person validateOwner(PersonaDocument duenio) {
    Person person = new Person();
    person.updateIdentification(duenio.getId());

		return duenio != null ? personaMapperMongo.fromAdapterToDomain(duenio) : new Person();
	}
}