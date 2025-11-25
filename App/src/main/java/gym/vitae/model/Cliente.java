package gym.vitae.model;

import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.model.enums.Genero;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(
    name = "clientes",
    uniqueConstraints = {
      @UniqueConstraint(columnNames = "codigo_cliente"),
      @UniqueConstraint(columnNames = "cedula")
    },
    indexes = {
      @Index(name = "idx_codigo", columnList = "codigo_cliente"),
      @Index(name = "idx_cedula", columnList = "cedula"),
      @Index(name = "idx_nombres", columnList = "nombres,apellidos"),
      @Index(name = "idx_estado", columnList = "estado")
    })
public class Cliente {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "codigo_cliente", nullable = false, unique = true, length = 20)
  private String codigoCliente;

  @Column(name = "nombres", nullable = false, length = 100)
  private String nombres;

  @Column(name = "apellidos", nullable = false, length = 100)
  private String apellidos;

  @Column(name = "cedula", nullable = false, unique = true, length = 10)
  private String cedula;

  @Enumerated(EnumType.STRING)
  @Column(name = "genero", length = 20)
  private Genero genero;

  @Column(name = "telefono", nullable = false, length = 10)
  private String telefono;

  @Column(name = "direccion", length = 100)
  private String direccion;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "fecha_nacimiento")
  private LocalDate fechaNacimiento;

  @Column(name = "contacto_emergencia", length = 100)
  private String contactoEmergencia;

  @Column(name = "telefono_emergencia", length = 10)
  private String telefonoEmergencia;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoCliente estado = EstadoCliente.ACTIVO;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Membresia> membresias = new HashSet<>();

  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Factura> facturas = new HashSet<>();

  @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<InscripcionesClase> inscripciones = new HashSet<>();

  public Cliente() {}

  public Cliente(
      String codigoCliente, String nombres, String apellidos, String cedula, String telefono) {
    this.codigoCliente = codigoCliente;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.cedula = cedula;
    this.telefono = telefono;
  }

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

  public String getCodigoCliente() {
    return codigoCliente;
  }

  public void setCodigoCliente(String codigoCliente) {
    this.codigoCliente = codigoCliente;
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

  public LocalDate getFechaNacimiento() {
    return fechaNacimiento;
  }

  public void setFechaNacimiento(LocalDate fechaNacimiento) {
    this.fechaNacimiento = fechaNacimiento;
  }

  public String getContactoEmergencia() {
    return contactoEmergencia;
  }

  public void setContactoEmergencia(String contactoEmergencia) {
    this.contactoEmergencia = contactoEmergencia;
  }

  public String getTelefonoEmergencia() {
    return telefonoEmergencia;
  }

  public void setTelefonoEmergencia(String telefonoEmergencia) {
    this.telefonoEmergencia = telefonoEmergencia;
  }

  public EstadoCliente getEstado() {
    return estado;
  }

  public void setEstado(EstadoCliente estado) {
    this.estado = estado;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Set<Membresia> getMembresias() {
    return membresias;
  }

  public void setMembresias(Set<Membresia> membresias) {
    this.membresias = membresias;
  }

  public Set<Factura> getFacturas() {
    return facturas;
  }

  public void setFacturas(Set<Factura> facturas) {
    this.facturas = facturas;
  }

  public Set<InscripcionesClase> getInscripciones() {
    return inscripciones;
  }

  public void setInscripciones(Set<InscripcionesClase> inscripciones) {
    this.inscripciones = inscripciones;
  }

  @Override
  public String toString() {
    return "Cliente{id="
        + id
        + ", codigo='"
        + codigoCliente
        + "', nombre='"
        + nombres
        + " "
        + apellidos
        + "'}";
  }
}
