package gym.vitae.repositories;

import gym.vitae.model.MovimientosInventario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovimientosInventarioRepository extends Repository<MovimientosInventario> {

    private MovimientosInventario mapRow(ResultSet rs) throws SQLException {
        MovimientosInventario m = new MovimientosInventario();
        m.setId(rs.getInt("id"));
        m.setProductoId(rs.getInt("producto_id"));
        m.setCantidad(rs.getInt("cantidad"));
        m.setTipo(rs.getString("tipo"));
        m.setDescripcion(rs.getString("descripcion"));
        m.setFecha(rs.getTimestamp("fecha"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }

    @Override
    public MovimientosInventario save(MovimientosInventario entity) throws SQLException {
        String sql = "INSERT INTO " +
                "movimientos_inventario (producto_id, cantidad, tipo, descripcion, fecha) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getProductoId());
            ps.setInt(2, entity.getCantidad());
            ps.setString(3, entity.getTipo());
            ps.setString(4, entity.getDescripcion());
            ps.setTimestamp(5, entity.getFecha());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(MovimientosInventario entity) throws SQLException {
        String sql = "UPDATE movimientos_inventario " +
                "SET producto_id=?, cantidad=?, tipo=?, descripcion=?, fecha=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getProductoId());
            ps.setInt(2, entity.getCantidad());
            ps.setString(3, entity.getTipo());
            ps.setString(4, entity.getDescripcion());
            ps.setTimestamp(5, entity.getFecha());
            ps.setInt(6, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM movimientos_inventario WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<MovimientosInventario> findById(int id) throws SQLException {
        String sql = "SELECT * FROM movimientos_inventario WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<MovimientosInventario> findAll() throws SQLException {
        String sql = "SELECT * FROM movimientos_inventario ORDER BY id";
        List<MovimientosInventario> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM movimientos_inventario";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM movimientos_inventario WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<MovimientosInventario> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM movimientos_inventario ORDER BY id LIMIT ? OFFSET ?";
        List<MovimientosInventario> list = new ArrayList<>();
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
