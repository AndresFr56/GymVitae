package gym.vitae.repositories;

import gym.vitae.model.Pagos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PagosRepository extends Repository<Pagos> {

    private Pagos mapRow(ResultSet rs) throws SQLException {
        Pagos p = new Pagos();
        p.setId(rs.getInt("id"));
        p.setFacturaId(rs.getInt("factura_id"));
        p.setMonto(rs.getBigDecimal("monto"));
        p.setFechaPago(rs.getDate("fecha_pago"));
        p.setReferencia(rs.getString("referencia"));
        p.setEstado(rs.getString("estado"));
        p.setObservaciones(rs.getString("observaciones"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    @Override
    public Pagos save(Pagos entity) throws SQLException {
        // Try calling stored procedure sp_registrar_pago(...) then fallback to insert
        String call = "{ CALL sp_registrar_pago(?, ?, ?) }";
        try (Connection conn = getConnection()) {
            try (CallableStatement cs = conn.prepareCall(call)) {
                cs.setInt(1, entity.getFacturaId());
                cs.setBigDecimal(2, entity.getMonto());
                cs.setString(3, entity.getReferencia());
                cs.execute();
                // attempt to find last payment for factura
                String lookup = "SELECT id, fecha_pago FROM pagos WHERE factura_id = ? ORDER BY id DESC LIMIT 1";
                try (PreparedStatement ps = conn.prepareStatement(lookup)) {
                    ps.setInt(1, entity.getFacturaId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            entity.setId(rs.getInt("id"));
                            entity.setFechaPago(rs.getDate("fecha_pago"));
                        }
                    }
                }
                return entity;
            } catch (SQLException ex) {
                // fallback to insert
            }

            String insert = "INSERT INTO pagos " +
                    "(factura_id, monto, fecha_pago, referencia, estado, observaciones) " +
                    "VALUES (?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, entity.getFacturaId());
                ps.setBigDecimal(2, entity.getMonto());
                ps.setDate(3, entity.getFechaPago());
                ps.setString(4, entity.getReferencia());
                ps.setString(5, entity.getEstado());
                ps.setString(6, entity.getObservaciones());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) entity.setId(rs.getInt(1));
                }
                return entity;
            }
        }
    }

    @Override
    public boolean update(Pagos entity) throws SQLException {
        String sql = "UPDATE pagos " +
                "SET factura_id=?, monto=?, fecha_pago=?, referencia=?, estado=?, observaciones=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getFacturaId());
            ps.setBigDecimal(2, entity.getMonto());
            ps.setDate(3, entity.getFechaPago());
            ps.setString(4, entity.getReferencia());
            ps.setString(5, entity.getEstado());
            ps.setString(6, entity.getObservaciones());
            ps.setInt(7, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM pagos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Pagos> findById(int id) throws SQLException {
        String sql = "SELECT * FROM pagos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Pagos> findAll() throws SQLException {
        String sql = "SELECT * FROM pagos ORDER BY id";
        List<Pagos> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM pagos";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM pagos WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Pagos> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM pagos ORDER BY id LIMIT ? OFFSET ?";
        List<Pagos> list = new ArrayList<>();
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
}
