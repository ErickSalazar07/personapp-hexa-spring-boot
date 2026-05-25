package co.edu.javeriana.as.personapp.application.usecase;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.in.StudyInputPort;
import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.UseCase;
import co.edu.javeriana.as.personapp.domain.Study;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class StudyUseCase implements StudyInputPort {

  private StudyOutputPort studyPersintence;

  @Override
  public void setPersintence(StudyOutputPort studyPersintence) {
    this.studyPersintence = studyPersintence;
  }

  @Override
  public Study create(Study study) {
    log.debug("Into create on Application Domain");
    return studyPersintence.save(study);
  }

  @Override
  public List<Study> findAll() {
    log.info("Output: " + studyPersintence.getClass());
    return studyPersintence.find();
  }
  
}
