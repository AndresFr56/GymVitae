package gym.vitae.repositories;

import gym.vitae.model.Productos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductosRepository extends Repository<Productos> {

    private Productos mapRow(ResultSet rs) throws SQLException {
        Productos p = new Productos();
        p.setId(rs.getInt("id"));
        p.setCategoriaId(rs.getInt("categoria_id"));
        p.setCodigo(rs.getString("codigo"));
        p.setNombre(rs.getString("nombre"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setPrecio(rs.getBigDecimal("precio"));
        p.setStock(rs.getInt("stock"));
        p.setProveedorId(rs.getInt("proveedor_id"));
        p.setActivo(rs.getBoolean("activo"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    @Override
    public Productos save(Productos entity) throws SQLException {
        String sql = "INSERT INTO productos " +
                "(categoria_id, codigo, nombre, descripcion, precio, stock, proveedor_id, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getCategoriaId() != null ? entity.getCategoriaId() : 0);
            ps.setString(2, entity.getCodigo());
            ps.setString(3, entity.getNombre());
            ps.setString(4, entity.getDescripcion());
            ps.setBigDecimal(5, entity.getPrecio());
            ps.setInt(6, entity.getStock() != null ? entity.getStock() : 0);
            if (entity.getProveedorId() != null) ps.setInt(7, entity.getProveedorId());
            else ps.setNull(7, Types.INTEGER);
            ps.setBoolean(8, entity.getActivo() != null ? entity.getActivo() : true);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Productos entity) throws SQLException {
        String sql = "UPDATE productos " +
                "SET categoria_id=?, codigo=?, nombre=?, descripcion=?, precio=?, stock=?, proveedor_id=?, activo=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getCategoriaId() != null ? entity.getCategoriaId() : 0);
            ps.setString(2, entity.getCodigo());
            ps.setString(3, entity.getNombre());
            ps.setString(4, entity.getDescripcion());
            ps.setBigDecimal(5, entity.getPrecio());
            ps.setInt(6, entity.getStock() != null ? entity.getStock() : 0);
            if (entity.getProveedorId() != null) ps.setInt(7, entity.getProveedorId());
            else ps.setNull(7, Types.INTEGER);
            ps.setBoolean(8, entity.getActivo() != null ? entity.getActivo() : true);
            ps.setInt(9, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Productos> findById(int id) throws SQLException {
        String sql = "SELECT * FROM productos WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Productos> findAll() throws SQLException {
        String sql = "SELECT * FROM productos ORDER BY id";
        List<Productos> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM productos WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Productos> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM productos ORDER BY id LIMIT ? OFFSET ?";
        List<Productos> list = new ArrayList<>();
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
