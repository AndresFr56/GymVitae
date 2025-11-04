package gym.vitae.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Membresias {
	private Integer id;
	private Integer clienteId;
	private Integer tipoMembresiaId;
	private Date fechaInicio;
	private Date fechaFin;
	private BigDecimal precioPagado;
	private String estado;
	private String observaciones;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getClienteId() {
		return clienteId;
	}

	public void setClienteId(Integer clienteId) {
		this.clienteId = clienteId;
	}

	public Integer getTipoMembresiaId() {
		return tipoMembresiaId;
	}

	public void setTipoMembresiaId(Integer tipoMembresiaId) {
		this.tipoMembresiaId = tipoMembresiaId;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public BigDecimal getPrecioPagado() {
		return precioPagado;
	}

	public void setPrecioPagado(BigDecimal precioPagado) {
		this.precioPagado = precioPagado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
