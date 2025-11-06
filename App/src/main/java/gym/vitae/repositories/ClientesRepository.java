package gym.vitae.repositories;

import gym.vitae.model.Clientes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClientesRepository extends Repository<Clientes> {

    private Clientes mapRow(ResultSet rs) throws SQLException {
        Clientes c = new Clientes();
        c.setId(rs.getInt("id"));
        c.setCodigoCliente(rs.getString("codigo_cliente"));
        c.setNombres(rs.getString("nombres"));
        c.setApellidos(rs.getString("apellidos"));
        c.setCedula(rs.getString("cedula"));
        c.setGenero(rs.getString("genero"));
        c.setTelefono(rs.getString("telefono"));
        c.setDireccion(rs.getString("direccion"));
        c.setEmail(rs.getString("email"));
        c.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
        c.setContactoEmergencia(rs.getString("contacto_emergencia"));
        c.setTelefonoEmergencia(rs.getString("telefono_emergencia"));
        c.setEstado(rs.getString("estado"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        c.setUpdatedAt(rs.getTimestamp("updated_at"));
        return c;
    }

    @Override
    public Clientes save(Clientes entity) throws SQLException {
        // Prefer SP sp_crear_cliente which returns cliente_id and codigo_cliente
        String call = "{ CALL sp_crear_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?) }";
        try (Connection conn = getConnection(); CallableStatement cs = conn.prepareCall(call)) {
            cs.setString(1, entity.getNombres());
            cs.setString(2, entity.getApellidos());
            cs.setString(3, entity.getCedula());
            cs.setString(4, entity.getGenero());
            cs.setString(5, entity.getTelefono());
            cs.setString(6, entity.getDireccion());
            cs.setString(7, entity.getEmail());
            cs.setDate(8, entity.getFechaNacimiento());
            cs.setString(9, entity.getContactoEmergencia());
            cs.setString(10, entity.getTelefonoEmergencia());
            boolean hasResult = cs.execute();
            // The procedure does a SELECT at the end with cliente_id and codigo_cliente
            while (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs != null && rs.next()) {
                        entity.setId(rs.getInt("cliente_id"));
                        entity.setCodigoCliente(rs.getString("codigo_cliente"));
                        return entity;
                    }
                }
                hasResult = cs.getMoreResults();
            }
        }

        // Fallback: direct INSERT (if SP not present)
        String sql = "INSERT INTO clientes (codigo_cliente, nombres, apellidos, cedula, genero, telefono, direccion, email, fecha_nacimiento, contacto_emergencia, telefono_emergencia, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getCodigoCliente());
            ps.setString(2, entity.getNombres());
            ps.setString(3, entity.getApellidos());
            ps.setString(4, entity.getCedula());
            ps.setString(5, entity.getGenero());
            ps.setString(6, entity.getTelefono());
            ps.setString(7, entity.getDireccion());
            ps.setString(8, entity.getEmail());
            ps.setDate(9, entity.getFechaNacimiento());
            ps.setString(10, entity.getContactoEmergencia());
            ps.setString(11, entity.getTelefonoEmergencia());
            ps.setString(12, entity.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Clientes entity) throws SQLException {
        String sql = "UPDATE clientes SET codigo_cliente=?, nombres=?, apellidos=?, cedula=?, genero=?, telefono=?, direccion=?, email=?, fecha_nacimiento=?, contacto_emergencia=?, telefono_emergencia=?, estado=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, entity.getCodigoCliente());
            ps.setString(2, entity.getNombres());
            ps.setString(3, entity.getApellidos());
            ps.setString(4, entity.getCedula());
            ps.setString(5, entity.getGenero());
            ps.setString(6, entity.getTelefono());
            ps.setString(7, entity.getDireccion());
            ps.setString(8, entity.getEmail());
            ps.setDate(9, entity.getFechaNacimiento());
            ps.setString(10, entity.getContactoEmergencia());
            ps.setString(11, entity.getTelefonoEmergencia());
            ps.setString(12, entity.getEstado());
            ps.setInt(13, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "UPDATE clientes SET estado = 'suspendido' WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Clientes> findById(int id) throws SQLException {
        String sql = "SELECT * FROM clientes WHERE id = ? AND estado = 'activo' ";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Clientes> findAll() throws SQLException {
        String sql = "SELECT * FROM clientes WHERE estado='activo' ORDER BY id";
        List<Clientes> list = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM clientes WHERE estado='activo'";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM clientes WHERE id = ? AND estado='activo' LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Clientes> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM clientes ORDER BY id LIMIT ? OFFSET ?";
        List<Clientes> list = new ArrayList<>();
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
