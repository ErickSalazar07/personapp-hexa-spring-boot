package co.edu.javeriana.as.personapp.terminal.mapper;

import co.edu.javeriana.as.personapp.common.annotations.Mapper;
import co.edu.javeriana.as.personapp.domain.Phone;
import co.edu.javeriana.as.personapp.terminal.model.TelefonoModelCli;

@Mapper
public class TelefonoMapperCli {
  
  public TelefonoModelCli fromDomainToAdapterCli(Phone phone) {
    TelefonoModelCli telefonoModelCli = new TelefonoModelCli();
    telefonoModelCli.setNumero(phone.getNumber());
    telefonoModelCli.setCompanhia(phone.getCompany());
    telefonoModelCli.setCcDuenhio(phone.getOwner().getIdentification());
    return telefonoModelCli;
  }
  
}
