package mx.uv.dsw.citas.dto;

import java.time.LocalDateTime;

public class CitaResponseDTO {

    private Integer id;
    private String estudiante;
    private String asesor;
    private String fechaHora;
    private String motivo;
    private String estado;
    private LocalDateTime fechaSolicitud;

    public CitaResponseDTO() {}

    public CitaResponseDTO(Integer id, String estudiante, String asesor,
                           String fechaHora, String motivo, String estado,
                           LocalDateTime fechaSolicitud) {
        this.id = id;
        this.estudiante = estudiante;
        this.asesor = asesor;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getEstudiante() { return estudiante; }
    public void setEstudiante(String estudiante) { this.estudiante = estudiante; }
    public String getAsesor() { return asesor; }
    public void setAsesor(String asesor) { this.asesor = asesor; }
    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }
    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}