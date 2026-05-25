package co.edu.javeriana.as.personapp.model.response;

import co.edu.javeriana.as.personapp.model.request.EstudioRequest;

public class EstudioResponse extends EstudioRequest {

	private String status;

	public EstudioResponse(String ccPersona, String idProfesion, String fechaGrado, String universidad, String database, String status) {
		super(ccPersona, idProfesion, fechaGrado, universidad, database);
		this.status = status;
	}

	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
}