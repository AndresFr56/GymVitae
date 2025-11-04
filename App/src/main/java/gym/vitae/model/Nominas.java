package gym.vitae.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Nominas {
	private Integer id;
	private Integer empleadoId;
	private Integer mes;
	private Integer anio;
	private BigDecimal salarioBase;
	private BigDecimal bonificaciones;
	private BigDecimal deducciones;
	private BigDecimal horasExtra;
	private BigDecimal valorHoraExtra;
	private BigDecimal totalPagar;
	private Date fechaPago;
	private String estado;
	private String observaciones;
	private Integer generadaPor;
	private Integer aprobadaPor;
	private Integer pagadaPor;
	private Timestamp createdAt;
	private Timestamp updatedAt;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Integer empleadoId) {
		this.empleadoId = empleadoId;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public BigDecimal getSalarioBase() {
		return salarioBase;
	}

	public void setSalarioBase(BigDecimal salarioBase) {
		this.salarioBase = salarioBase;
	}

	public BigDecimal getBonificaciones() {
		return bonificaciones;
	}

	public void setBonificaciones(BigDecimal bonificaciones) {
		this.bonificaciones = bonificaciones;
	}

	public BigDecimal getDeducciones() {
		return deducciones;
	}

	public void setDeducciones(BigDecimal deducciones) {
		this.deducciones = deducciones;
	}

	public BigDecimal getHorasExtra() {
		return horasExtra;
	}

	public void setHorasExtra(BigDecimal horasExtra) {
		this.horasExtra = horasExtra;
	}

	public BigDecimal getValorHoraExtra() {
		return valorHoraExtra;
	}

	public void setValorHoraExtra(BigDecimal valorHoraExtra) {
		this.valorHoraExtra = valorHoraExtra;
	}

	public BigDecimal getTotalPagar() {
		return totalPagar;
	}

	public void setTotalPagar(BigDecimal totalPagar) {
		this.totalPagar = totalPagar;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
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

	public Integer getGeneradaPor() {
		return generadaPor;
	}

	public void setGeneradaPor(Integer generadaPor) {
		this.generadaPor = generadaPor;
	}

	public Integer getAprobadaPor() {
		return aprobadaPor;
	}

	public void setAprobadaPor(Integer aprobadaPor) {
		this.aprobadaPor = aprobadaPor;
	}

	public Integer getPagadaPor() {
		return pagadaPor;
	}

	public void setPagadaPor(Integer pagadaPor) {
		this.pagadaPor = pagadaPor;
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
