package mx.uv.dsw.citas.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cita")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estudiante", nullable = false)
    private Usuario estudiante;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disponibilidad", nullable = false, unique = true)
    private Disponibilidad disponibilidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_motivo", nullable = false)
    private Motivo motivo;

    @Column(nullable = false, length = 20)
    private String estado = "SOLICITADA";

    @Column(name = "fecha_solicitud")
    private LocalDateTime fechaSolicitud;

    @Column(length = 255)
    private String observaciones;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Usuario getEstudiante() { return estudiante; }
    public void setEstudiante(Usuario estudiante) { this.estudiante = estudiante; }
    public Disponibilidad getDisponibilidad() { return disponibilidad; }
    public void setDisponibilidad(Disponibilidad disponibilidad) { this.disponibilidad = disponibilidad; }
    public Motivo getMotivo() { return motivo; }
    public void setMotivo(Motivo motivo) { this.motivo = motivo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDateTime fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}