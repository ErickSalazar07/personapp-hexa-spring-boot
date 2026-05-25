package co.edu.javeriana.as.personapp.mariadb.adapter;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mariadb.entity.EstudiosEntity;
import co.edu.javeriana.as.personapp.mariadb.mapper.EstudiosMapperMaria;
import co.edu.javeriana.as.personapp.mariadb.repository.EstudiosRepositoryMaria;
import lombok.extern.slf4j.Slf4j;

@Primary
@Slf4j
@Adapter("studyOutputAdapterMaria")
@Transactional
public class StudyOutputAdapterMaria implements StudyOutputPort {
  @Autowired
  private EstudiosRepositoryMaria estudiosRepositoryMaria;

  @Autowired
  private EstudiosMapperMaria estudiosMapperMaria;

  @Override
  public Study save(Study study) {
    log.debug("Into save on Adapter MariaDB");
    EstudiosEntity persistedStudy = estudiosRepositoryMaria.save(estudiosMapperMaria.fromDomainToAdapter(study));
    return estudiosMapperMaria.fromAdapterToDomain(persistedStudy);
  }

  @Override
  public List<Study> find() {
    log.debug("Into find on Adapter MariaDB");
    return estudiosRepositoryMaria.findAll().stream().map(estudiosMapperMaria::fromAdapterToDomain)
        .collect(Collectors.toList());
  }
  
}
