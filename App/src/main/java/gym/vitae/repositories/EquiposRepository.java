package gym.vitae.repositories;

import gym.vitae.model.Equipos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EquiposRepository extends Repository<Equipos> {

    private Equipos mapRow(ResultSet rs) throws SQLException {
        Equipos e = new Equipos();
        e.setId(rs.getInt("id"));
        e.setCategoriaId(rs.getInt("categoria_id"));
        e.setNombre(rs.getString("nombre"));
        e.setMarca(rs.getString("marca"));
        e.setModelo(rs.getString("modelo"));
        e.setSerial(rs.getString("serial"));
        e.setFechaCompra(rs.getDate("fecha_compra"));
        e.setEstado(rs.getString("estado"));
        e.setUbicacion(rs.getString("ubicacion"));
        e.setCreatedAt(rs.getTimestamp("created_at"));
        e.setUpdatedAt(rs.getTimestamp("updated_at"));
        return e;
    }

    @Override
    public Equipos save(Equipos entity) throws SQLException {
        String sql = "INSERT INTO equipos (categoria_id, nombre, marca, modelo, serial, fecha_compra, estado, ubicacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getCategoriaId() != null ? entity.getCategoriaId() : 0);
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getMarca());
            ps.setString(4, entity.getModelo());
            ps.setString(5, entity.getSerial());
            ps.setDate(6, entity.getFechaCompra());
            ps.setString(7, entity.getEstado());
            ps.setString(8, entity.getUbicacion());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Equipos entity) throws SQLException {
        String sql = "UPDATE equipos SET categoria_id=?, nombre=?, marca=?, modelo=?, serial=?, fecha_compra=?, estado=?, ubicacion=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getCategoriaId() != null ? entity.getCategoriaId() : 0);
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getMarca());
            ps.setString(4, entity.getModelo());
            ps.setString(5, entity.getSerial());
            ps.setDate(6, entity.getFechaCompra());
            ps.setString(7, entity.getEstado());
            ps.setString(8, entity.getUbicacion());
            ps.setInt(9, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM equipos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Equipos> findById(int id) throws SQLException {
        String sql = "SELECT * FROM equipos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Equipos> findAll() throws SQLException {
        String sql = "SELECT * FROM equipos ORDER BY id";
        List<Equipos> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM equipos";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM equipos WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Equipos> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM equipos ORDER BY id LIMIT ? OFFSET ?";
        List<Equipos> list = new ArrayList<>();
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
