package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Person;
import co.edu.javeriana.as.personapp.domain.Profession;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.document.PersonaDocument;
import co.edu.javeriana.as.personapp.mongo.document.ProfesionDocument;
import lombok.NonNull;

@Mapper
public class EstudiosMapperMongo {

	public EstudiosDocument fromDomainToAdapter(Study study) {
		EstudiosDocument studyDocument = new EstudiosDocument();

		studyDocument.setId(validateId(study.getPerson().getIdentification(), study.getProfession().getIdentification()));
		studyDocument.setPrimaryPersona(validatePrimaryPersona(study.getPerson()));
		studyDocument.setPrimaryProfesion(validatePrimaryProfesion(study.getProfession()));
		studyDocument.setFecha(validateFecha(study.getGraduationDate()));
		studyDocument.setUniver(validateUniver(study.getUniversityName()));

		return studyDocument;
	}

	private String validateId(@NonNull Integer identificationPerson, @NonNull Integer identificationProfession) {
		return identificationPerson + "-" + identificationProfession;
	}

  private PersonaDocument validatePrimaryPersona(@NonNull Person person) {
    PersonaDocument persona = new PersonaDocument();

    persona.setId(person.getIdentification());
    persona.setNombre(person.getFirstName());

    return persona;
  }

  private ProfesionDocument validatePrimaryProfesion(@NonNull Profession profession) {
    ProfesionDocument profesion = new ProfesionDocument();

    profesion.setId(profession.getIdentification());
    profesion.setNom(profession.getName());

    return profesion;
  }

	private LocalDate validateFecha(LocalDate graduationDate) {
		return graduationDate != null ? graduationDate : null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study();
    Person person = new Person();
    Profession profession = new Profession();

    person.updateIdentification(estudiosDocument.getPrimaryPersona().getId());
    profession.updateIdentification(estudiosDocument.getPrimaryProfesion().getId());

		study.updatePerson(person);
		study.updateProfession(profession);
		study.updateGraduationDate(validateGraduationDate(estudiosDocument.getFecha()));
		study.updateUniversityName(validateUniversityName(estudiosDocument.getUniver()));

		return study;
	}

	private LocalDate validateGraduationDate(LocalDate fecha) {
		return fecha != null ? fecha : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}
