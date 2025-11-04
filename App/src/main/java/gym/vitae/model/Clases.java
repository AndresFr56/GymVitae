package gym.vitae.model;

import java.sql.Timestamp;

public class Clases {
	private Integer id;
	private String nombre;
	private String descripcion;
	private Integer duracionMinutos;
	private Integer capacidadMaxima;
	private String nivel;
	private Boolean activa;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Integer getDuracionMinutos() {
		return duracionMinutos;
	}

	public void setDuracionMinutos(Integer duracionMinutos) {
		this.duracionMinutos = duracionMinutos;
	}

	public Integer getCapacidadMaxima() {
		return capacidadMaxima;
	}

	public void setCapacidadMaxima(Integer capacidadMaxima) {
		this.capacidadMaxima = capacidadMaxima;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public Boolean getActiva() {
		return activa;
	}

	public void setActiva(Boolean activa) {
		this.activa = activa;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public Timestamp getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
}
