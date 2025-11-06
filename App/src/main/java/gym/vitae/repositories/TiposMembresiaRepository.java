package gym.vitae.repositories;

import gym.vitae.model.TiposMembresia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TiposMembresiaRepository extends Repository<TiposMembresia>{

    private TiposMembresia mapRow(ResultSet rs) throws SQLException {
        TiposMembresia t = new TiposMembresia();
        t.setId(rs.getInt("id"));
        t.setNombre(rs.getString("nombre"));
        t.setDuracionMeses(rs.getInt("duracion_meses"));
        t.setPrecio(rs.getBigDecimal("precio"));
        t.setDescripcion(rs.getString("descripcion"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setUpdatedAt(rs.getTimestamp("updated_at"));
        return t;
    }

    @Override
    public TiposMembresia save(TiposMembresia entity) throws SQLException {
        String sql = "INSERT INTO tipos_membresia (nombre, duracion_meses, precio, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNombre());
            ps.setInt(2, entity.getDuracionMeses() != null ? entity.getDuracionMeses() : 0);
            ps.setBigDecimal(3, entity.getPrecio());
            ps.setString(4, entity.getDescripcion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(TiposMembresia entity) throws SQLException {
        String sql = "UPDATE tipos_membresia SET nombre=?, duracion_meses=?, precio=?, descripcion=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getNombre());
            ps.setInt(2, entity.getDuracionMeses() != null ? entity.getDuracionMeses() : 0);
            ps.setBigDecimal(3, entity.getPrecio());
            ps.setString(4, entity.getDescripcion());
            ps.setInt(5, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM tipos_membresia WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<TiposMembresia> findById(int id) throws SQLException {
        String sql = "SELECT * FROM tipos_membresia WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<TiposMembresia> findAll() throws SQLException {
        String sql = "SELECT * FROM tipos_membresia ORDER BY id";
        List<TiposMembresia> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM tipos_membresia";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM tipos_membresia WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<TiposMembresia> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM tipos_membresia ORDER BY id LIMIT ? OFFSET ?";
        List<TiposMembresia> list = new ArrayList<>();
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
