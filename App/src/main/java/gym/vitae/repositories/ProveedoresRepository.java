package gym.vitae.repositories;

import gym.vitae.model.Proveedores;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProveedoresRepository extends Repository<Proveedores>{

    private Proveedores mapRow(ResultSet rs) throws SQLException {
        Proveedores p = new Proveedores();
        p.setId(rs.getInt("id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setContacto(rs.getString("contacto"));
        p.setTelefono(rs.getString("telefono"));
        p.setEmail(rs.getString("email"));
        p.setDireccion(rs.getString("direccion"));
        p.setActivo(rs.getBoolean("activo"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    @Override
    public Proveedores save(Proveedores entity) throws SQLException {
        String insert = "INSERT INTO proveedores (codigo, nombre, contacto, telefono, email, direccion, activo) " +
                "VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCodigo());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getContacto());
            ps.setString(4, entity.getTelefono());
            ps.setString(5, entity.getEmail());
            ps.setString(6, entity.getDireccion());
            ps.setBoolean(7, entity.getActivo() != null ? entity.getActivo() : false);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Proveedores entity) throws SQLException {
        String sql = "UPDATE proveedores " +
                "SET codigo=?, nombre=?, contacto=?, telefono=?, email=?, direccion=?, activo=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigo());
            ps.setString(2, entity.getNombre());
            ps.setString(3, entity.getContacto());
            ps.setString(4, entity.getTelefono());
            ps.setString(5, entity.getEmail());
            ps.setString(6, entity.getDireccion());
            ps.setBoolean(7, entity.getActivo() != null ? entity.getActivo() : false);
            ps.setInt(8, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "UPDATE proveedores SET activo = false WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Proveedores> findById(int id) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE id = ? AND activo=true LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Proveedores> findAll() throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE activo=true ORDER BY id";
        List<Proveedores> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM proveedores WHERE activo=true";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM proveedores WHERE id = ? AND activo=true LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Proveedores> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM proveedores WHERE activo=true ORDER BY id LIMIT ? OFFSET ?";
        List<Proveedores> list = new ArrayList<>();
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
