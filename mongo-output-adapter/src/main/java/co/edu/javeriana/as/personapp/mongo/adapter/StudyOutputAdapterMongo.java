package co.edu.javeriana.as.personapp.mongo.adapter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoWriteException;

import co.edu.javeriana.as.personapp.application.port.out.StudyOutputPort;
import co.edu.javeriana.as.personapp.common.annotations.Adapter;
import co.edu.javeriana.as.personapp.domain.Study;
import co.edu.javeriana.as.personapp.mongo.document.EstudiosDocument;
import co.edu.javeriana.as.personapp.mongo.mapper.EstudiosMapperMongo;
import co.edu.javeriana.as.personapp.mongo.repository.EstudiosRepositoryMongo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Adapter("studyOutputAdapterMongo")
public class StudyOutputAdapterMongo implements StudyOutputPort {
  @Autowired
  private EstudiosRepositoryMongo estudiosRepositoryMongo;

  @Autowired
  private EstudiosMapperMongo estudiosMapperMongo;

  @Override
  public Study save(Study study) {
    log.debug("Into save on Adapter MongoDB");
    try {
      EstudiosDocument persistedStudy = estudiosRepositoryMongo.save(estudiosMapperMongo.fromDomainToAdapter(study));
      return estudiosMapperMongo.fromAdapterToDomain(persistedStudy);
    } catch (MongoWriteException e) {
      log.warn(e.getMessage());
      return study;
    }
  }

  @Override
  public List<Study> find() {
    log.debug("Into find on Adapter MongoDB");
    return estudiosRepositoryMongo.findAll().stream().map(estudiosMapperMongo::fromAdapterToDomain)
        .collect(Collectors.toList());
  }
  
}
