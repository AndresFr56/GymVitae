package gym.vitae.model;

import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import gym.vitae.model.enums.TipoContrato;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(
    name = "empleados",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "codigo_empleado"),
      @UniqueConstraint(columnNames = "cedula"),
      @UniqueConstraint(columnNames = "email")
    },
    indexes = {
      @Index(name = "idx_codigo", columnList = "codigo_empleado"),
      @Index(name = "idx_cedula", columnList = "cedula"),
      @Index(name = "idx_estado", columnList = "estado"),
      @Index(name = "idx_nombres", columnList = "nombres,apellidos")
    })
public class Empleado {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cargo_id", nullable = false)
  private Cargo cargo;

  @Column(name = "codigo_empleado", nullable = false, unique = true, length = 20)
  private String codigoEmpleado;

  @Column(name = "nombres", nullable = false, length = 100)
  private String nombres;

  @Column(name = "apellidos", nullable = false, length = 100)
  private String apellidos;

  @Column(name = "cedula", nullable = false, unique = true, length = 10)
  private String cedula;

  @Enumerated(EnumType.STRING)
  @Column(name = "genero", nullable = false, length = 20)
  private Genero genero;

  @Column(name = "telefono", nullable = false, length = 10)
  private String telefono;

  @Column(name = "direccion", length = 100)
  private String direccion;

  @Column(name = "email", unique = true, length = 100)
  private String email;

  @Column(name = "fecha_ingreso", nullable = false)
  private LocalDate fechaIngreso;

  @Column(name = "fecha_salida")
  private LocalDate fechaSalida;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_contrato", nullable = false, length = 20)
  private TipoContrato tipoContrato;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoEmpleado estado;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Nomina> nominas = new HashSet<>();

  @OneToMany(mappedBy = "empleadoResponsable", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Factura> facturas = new HashSet<>();

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Cargo getCargo() {
    return cargo;
  }

  public void setCargo(Cargo cargo) {
    this.cargo = cargo;
  }

  public String getCodigoEmpleado() {
    return codigoEmpleado;
  }

  public void setCodigoEmpleado(String codigoEmpleado) {
    this.codigoEmpleado = codigoEmpleado;
  }

  public String getNombres() {
    return nombres;
  }

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }

  public String getApellidos() {
    return apellidos;
  }

  public void setApellidos(String apellidos) {
    this.apellidos = apellidos;
  }

  public String getCedula() {
    return cedula;
  }

  public void setCedula(String cedula) {
    this.cedula = cedula;
  }

  public Genero getGenero() {
    return genero;
  }

  public void setGenero(Genero genero) {
    this.genero = genero;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public LocalDate getFechaSalida() {
    return fechaSalida;
  }

  public void setFechaSalida(LocalDate fechaSalida) {
    this.fechaSalida = fechaSalida;
  }

  public TipoContrato getTipoContrato() {
    return tipoContrato;
  }

  public void setTipoContrato(TipoContrato tipoContrato) {
    this.tipoContrato = tipoContrato;
  }

  public EstadoEmpleado getEstado() {
    return estado;
  }

  public void setEstado(EstadoEmpleado estado) {
    this.estado = estado;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Set<Nomina> getNominas() {
    return nominas;
  }

  public void setNominas(Set<Nomina> nominas) {
    this.nominas = nominas;
  }

  public Set<Factura> getFacturas() {
    return facturas;
  }

  public void setFacturas(Set<Factura> facturas) {
    this.facturas = facturas;
  }

  @Override
  public String toString() {
    return "Empleado{id="
        + id
        + ", codigo='"
        + codigoEmpleado
        + "', nombre='"
        + nombres
        + " "
        + apellidos
        + "'}";
  }
}
