package mx.uv.dsw.citas.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class DisponibilidadDTO {

    private Integer id;
    private String asesor;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String estado;

    public DisponibilidadDTO() {}

    public DisponibilidadDTO(Integer id, String asesor, LocalDate fecha,
                             LocalTime horaInicio, LocalTime horaFin, String estado) {
        this.id = id;
        this.asesor = asesor;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.estado = estado;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getAsesor() { return asesor; }
    public void setAsesor(String asesor) { this.asesor = asesor; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }
    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}