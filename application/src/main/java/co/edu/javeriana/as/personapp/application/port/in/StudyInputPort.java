package co.edu.javeriana.as.personapp.application.port.in;

import java.util.List;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Port;
import co.edu.javeriana.as.personapp.domain.Study;

@Port
public interface StudyInputPort {
	public void setPersintence(StudyOutputPort studyPersintence);
	public Study create(Study person);
	public List<Study> findAll();
}
