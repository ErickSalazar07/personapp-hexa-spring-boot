package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.terminal.model.EstudioModelCli;

@Mapper
public class EstudioMapperCli {

  public EstudioModelCli fromDomainToAdapterCli(Study study) {
    EstudioModelCli estudioModelCli = new EstudioModelCli();
    estudioModelCli.setCcPersona(study.getPerson().getIdentification());
    estudioModelCli.setFechaGraduacion(study.getGraduationDate());
    estudioModelCli.setIdProfesion(study.getProfession().getIdentification());
    estudioModelCli.setNombreUniversidad(study.getUniversityName());
    return estudioModelCli;
  }
  
}
