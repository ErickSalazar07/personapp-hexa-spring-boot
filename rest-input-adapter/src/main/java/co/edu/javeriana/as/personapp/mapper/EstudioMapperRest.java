package co.edu.javeriana.as.personapp.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.model.request.EstudioRequest;
import co.edu.javeriana.as.personapp.model.response.EstudioResponse;

import java.time.LocalDate;

@Mapper
public class EstudioMapperRest {

	public EstudioResponse fromDomainToAdapterRestMaria(Study study) {
		return fromDomainToAdapterRest(study, "MariaDB");
	}

	public EstudioResponse fromDomainToAdapterRestMongo(Study study) {
		return fromDomainToAdapterRest(study, "MongoDB");
	}

	public EstudioResponse fromDomainToAdapterRest(Study study, String database) {
		String cc = study.getPerson() != null ? study.getPerson().getIdentification() + "" : "";
		String idProf = study.getProfession() != null ? study.getProfession().getIdentification() + "" : "";
		String fecha = study.getGraduationDate() != null ? study.getGraduationDate().toString() : "";
		return new EstudioResponse(
				cc,
				idProf,
				fecha,
				study.getUniversityName(),
				database,
				"OK");
	}

	public Study fromAdapterToDomain(EstudioRequest request) {
		Person person = new Person(
				Integer.parseInt(request.getCcPersona()),
				null, null, null, null, null, null);
		Profession profession = new Profession(
				Integer.parseInt(request.getIdProfesion()),
				null, null, null);
		LocalDate fecha = null;
		if (request.getFechaGrado() != null && !request.getFechaGrado().isBlank()) {
			fecha = LocalDate.parse(request.getFechaGrado());
		}
		return new Study(person, profession, fecha, request.getUniversidad());
	}
}