package gym.vitae.repositories;

import gym.vitae.model.Facturacion;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacturacionRepository extends Repository<Facturacion> {

    private Facturacion mapRow(ResultSet rs) throws SQLException {
        Facturacion f = new Facturacion();
        f.setId(rs.getInt("id"));
        f.setNumeroFactura(rs.getString("numero_factura"));
        f.setClienteId(rs.getInt("cliente_id"));
        f.setEmpleadoResponsableId(rs.getInt("empleado_responsable_id"));
        f.setFechaEmision(rs.getDate("fecha_emision"));
        f.setTipoVenta(rs.getString("tipo_venta"));
        f.setTotal(rs.getBigDecimal("total"));
        f.setEstado(rs.getString("estado"));
        f.setObservaciones(rs.getString("observaciones"));
        f.setCreatedAt(rs.getTimestamp("created_at"));
        f.setUpdatedAt(rs.getTimestamp("updated_at"));
        return f;
    }

    @Override
    public Facturacion save(Facturacion entity) throws SQLException {
        String sql = "INSERT INTO facturas (numero_factura, cliente_id, empleado_responsable_id, fecha_emision, tipo_venta, total, estado, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNumeroFactura());
            ps.setInt(2, entity.getClienteId());
            ps.setInt(3, entity.getEmpleadoResponsableId());
            ps.setDate(4, entity.getFechaEmision());
            ps.setString(5, entity.getTipoVenta());
            ps.setBigDecimal(6, entity.getTotal());
            ps.setString(7, entity.getEstado());
            ps.setString(8, entity.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Facturacion entity) throws SQLException {
        String sql = "UPDATE facturas SET numero_factura=?, cliente_id=?, empleado_responsable_id=?, fecha_emision=?, tipo_venta=?, total=?, estado=?, observaciones=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNumeroFactura());
            ps.setInt(2, entity.getClienteId());
            ps.setInt(3, entity.getEmpleadoResponsableId());
            ps.setDate(4, entity.getFechaEmision());
            ps.setString(5, entity.getTipoVenta());
            ps.setBigDecimal(6, entity.getTotal());
            ps.setString(7, entity.getEstado());
            ps.setString(8, entity.getObservaciones());
            ps.setInt(9, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM facturas WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Facturacion> findById(int id) throws SQLException {
        String sql = "SELECT * FROM facturas WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Facturacion> findAll() throws SQLException {
        String sql = "SELECT * FROM facturas ORDER BY id";
        List<Facturacion> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM facturas";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM facturas WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Facturacion> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM facturas ORDER BY id LIMIT ? OFFSET ?";
        List<Facturacion> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    // Example: read pending invoices view
    public List<Facturacion> findPendingInvoices() throws SQLException {
        String sql = "SELECT id, numero_factura, cliente_id, empleado_responsable_id, fecha_emision, tipo_venta, total, estado, observaciones, created_at, updated_at FROM v_facturas_pendientes";
        List<Facturacion> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }
}
