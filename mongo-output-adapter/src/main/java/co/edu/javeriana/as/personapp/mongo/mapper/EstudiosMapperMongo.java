package co.edu.javeriana.as.personapp.mongo.mapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

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
	@Autowired
	private PersonaMapperMongo personaMapperMongo;

	@Autowired
	private ProfesionMapperMongo profesionMapperMongo;

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
		return person != null ? personaMapperMongo.fromDomainToAdapter(person) : new PersonaDocument();
	}

	private ProfesionDocument validatePrimaryProfesion(@NonNull Profession profession) {
		return profession != null ? profesionMapperMongo.fromDomainToAdapter(profession) : new ProfesionDocument();
	}

	private LocalDate validateFecha(LocalDate graduationDate) {
		return graduationDate != null ? graduationDate : null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosDocument estudiosDocument) {
		Study study = new Study();
		study.updatePerson(personaMapperMongo.fromAdapterToDomain(estudiosDocument.getPrimaryPersona()));
		study.updateProfession(profesionMapperMongo.fromAdapterToDomain(estudiosDocument.getPrimaryProfesion()));
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
