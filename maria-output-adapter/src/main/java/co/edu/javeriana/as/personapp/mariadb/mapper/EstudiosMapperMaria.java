package co.edu.javeriana.as.personapp.mariadb.mapper;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntityPK;

@Mapper
public class EstudiosMapperMaria {
	@Autowired
	private PersonaMapperMaria personaMapperMaria;

	@Autowired
	private ProfesionMapperMaria profesionMapperMaria;

	public EstudiosEntity fromDomainToAdapter(Study study) {
		EstudiosEntityPK studyPK = new EstudiosEntityPK();
		studyPK.setCcPer(study.getPerson().getIdentification());
		studyPK.setIdProf(study.getProfession().getIdentification());
		EstudiosEntity studyEntity = new EstudiosEntity();
		studyEntity.setEstudiosPK(studyPK);
		studyEntity.setFecha(validateFecha(study.getGraduationDate()));
		studyEntity.setUniver(validateUniver(study.getUniversityName()));
		return studyEntity;
	}

	private Date validateFecha(LocalDate graduationDate) {
		return graduationDate != null
				? Date.from(graduationDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant())
				: null;
	}

	private String validateUniver(String universityName) {
		return universityName != null ? universityName : "";
	}

	public Study fromAdapterToDomain(EstudiosEntity estudiosEntity) {
		Study study = new Study();
		study.updatePerson(personaMapperMaria.fromAdapterToDomain(estudiosEntity.getPersona()));
		study.updateProfession(profesionMapperMaria.fromAdapterToDomain(estudiosEntity.getProfesion()));
		study.updateGraduationDate(validateGraduationDate(estudiosEntity.getFecha()));
		study.updateUniversityName(validateUniversityName(estudiosEntity.getUniver()));
		return study;
	}

	private LocalDate validateGraduationDate(Date fecha) {
		return fecha != null ? fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
	}

	private String validateUniversityName(String univer) {
		return univer != null ? univer : "";
	}
}
