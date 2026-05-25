package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Gender;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;

@Mapper
public class PersonaMapperRest {
	public PersonaResponse fromDomainToAdapterRestMaria(Person person) {
		return fromDomainToAdapterRest(person, "MariaDB");
	}
	public PersonaResponse fromDomainToAdapterRestMongo(Person person) {
		return fromDomainToAdapterRest(person, "MongoDB");
	}
	
	public PersonaResponse fromDomainToAdapterRest(Person person, String database) {
		return new PersonaResponse(
				person.getIdentification()+"", 
				person.getFirstName(), 
				person.getLastName(), 
				person.getAge()+"", 
				person.getGender().toString(), 
				database,
				"OK");
	}

	//arreglao
	public Person fromAdapterToDomain(PersonaRequest request) {
		Gender gender = (request.getSex() != null && request.getSex().equalsIgnoreCase("MALE"))
				? Gender.MALE : Gender.FEMALE;
		Integer age = null;
		try {
			age = Integer.parseInt(request.getAge());
		} catch (NumberFormatException e) {
			age = 0;
		}
		return new Person(
				Integer.parseInt(request.getDni()),
				request.getFirstName(),
				request.getLastName(),
				gender,
				age,
				null,
				null);
	}

}
