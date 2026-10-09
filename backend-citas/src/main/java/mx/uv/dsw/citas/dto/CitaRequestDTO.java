package mx.uv.dsw.citas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CitaRequestDTO {

    @NotNull
    private Integer idEstudiante;

    @NotNull
    private Integer idDisponibilidad;

    @NotNull
    private Integer idMotivo;

    @Size(max = 255)
    private String observaciones;

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }
    public Integer getIdDisponibilidad() { return idDisponibilidad; }
    public void setIdDisponibilidad(Integer idDisponibilidad) { this.idDisponibilidad = idDisponibilidad; }
    public Integer getIdMotivo() { return idMotivo; }
    public void setIdMotivo(Integer idMotivo) { this.idMotivo = idMotivo; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}