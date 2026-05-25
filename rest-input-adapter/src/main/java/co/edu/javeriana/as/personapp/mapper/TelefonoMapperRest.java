package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.model.request.TelefonoRequest;
import co.edu.javeriana.as.personapp.model.response.TelefonoResponse;

@Mapper
public class TelefonoMapperRest {

	public TelefonoResponse fromDomainToAdapterRestMaria(Phone phone) {
		return fromDomainToAdapterRest(phone, "MariaDB");
	}

	public TelefonoResponse fromDomainToAdapterRestMongo(Phone phone) {
		return fromDomainToAdapterRest(phone, "MongoDB");
	}

	public TelefonoResponse fromDomainToAdapterRest(Phone phone, String database) {
		String ownerCc = phone.getOwner() != null ? phone.getOwner().getIdentification() + "" : "";
		return new TelefonoResponse(
				phone.getNumber(),
				phone.getCompany(),
				ownerCc,
				database,
				"OK");
	}

	public Phone fromAdapterToDomain(TelefonoRequest request) {
		Person owner = new Person(
				Integer.parseInt(request.getDuenioCc()),
				null, null, null, null, null, null);
		return new Phone(
				request.getNumero(),
				request.getOperador(),
				owner);
	}
}