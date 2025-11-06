package gym.vitae.repositories;

import gym.vitae.model.DetalleFactura;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetallesFacturaRepository extends Repository<DetalleFactura> {

    private DetalleFactura mapRow(ResultSet rs) throws SQLException {
        DetalleFactura d = new DetalleFactura();
        d.setId(rs.getInt("id"));
        d.setFacturaId(rs.getInt("factura_id"));
        d.setProductoId(rs.getInt("producto_id"));
        d.setCantidad(rs.getInt("cantidad"));
        d.setPrecioUnitario(rs.getBigDecimal("precio_unitario"));
        d.setSubtotal(rs.getBigDecimal("subtotal"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        return d;
    }

    @Override
    public DetalleFactura save(DetalleFactura entity) throws SQLException {
        String sql = "INSERT INTO detalles_factura (factura_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getFacturaId());
            ps.setInt(2, entity.getProductoId());
            ps.setInt(3, entity.getCantidad());
            ps.setBigDecimal(4, entity.getPrecioUnitario());
            ps.setBigDecimal(5, entity.getSubtotal());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(DetalleFactura entity) throws SQLException {
        String sql = "UPDATE detalles_factura SET factura_id=?, producto_id=?, cantidad=?, precio_unitario=?, subtotal=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getFacturaId());
            ps.setInt(2, entity.getProductoId());
            ps.setInt(3, entity.getCantidad());
            ps.setBigDecimal(4, entity.getPrecioUnitario());
            ps.setBigDecimal(5, entity.getSubtotal());
            ps.setInt(6, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM detalles_factura WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<DetalleFactura> findById(int id) throws SQLException {
        String sql = "SELECT * FROM detalles_factura WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<DetalleFactura> findAll() throws SQLException {
        String sql = "SELECT * FROM detalles_factura ORDER BY id";
        List<DetalleFactura> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM detalles_factura";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM detalles_factura WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<DetalleFactura> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM detalles_factura ORDER BY id LIMIT ? OFFSET ?";
        List<DetalleFactura> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }
}
