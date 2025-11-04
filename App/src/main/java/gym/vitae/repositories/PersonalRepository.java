package gym.vitae.repositories;

import gym.vitae.model.Personal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PersonalRepository extends Repository<Personal> {

    private Personal mapRow(ResultSet rs) throws SQLException {
        Personal p = new Personal();
        p.setId(rs.getInt("id"));
        p.setCargoId(rs.getInt("cargo_id"));
        p.setCodigoEmpleado(rs.getString("codigo_empleado"));
        p.setNombres(rs.getString("nombres"));
        p.setApellidos(rs.getString("apellidos"));
        p.setCedula(rs.getString("cedula"));
        p.setGenero(rs.getString("genero"));
        p.setTelefono(rs.getString("telefono"));
        p.setDireccion(rs.getString("direccion"));
        p.setEmail(rs.getString("email"));
        p.setFechaIngreso(rs.getDate("fecha_ingreso"));
        p.setFechaSalida(rs.getDate("fecha_salida"));
        p.setTipoContrato(rs.getString("tipo_contrato"));
        p.setEstado(rs.getString("estado"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }

    @Override
    public Personal save(Personal entity) throws SQLException {
        String insert = "INSERT INTO empleados " +
                "(cargo_id, codigo_empleado, nombres, apellidos, cedula, genero, telefono, direccion, email, fecha_ingreso, fecha_salida, tipo_contrato, estado) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getCargoId());
            ps.setString(2, entity.getCodigoEmpleado());
            ps.setString(3, entity.getNombres());
            ps.setString(4, entity.getApellidos());
            ps.setString(5, entity.getCedula());
            ps.setString(6, entity.getGenero());
            ps.setString(7, entity.getTelefono());
            ps.setString(8, entity.getDireccion());
            ps.setString(9, entity.getEmail());
            ps.setDate(10, entity.getFechaIngreso());
            ps.setDate(11, entity.getFechaSalida());
            ps.setString(12, entity.getTipoContrato());
            ps.setString(13, entity.getEstado());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Personal entity) throws SQLException {
        String sql = "UPDATE empleados " +
                "SET cargo_id=?, codigo_empleado=?, nombres=?, apellidos=?, cedula=?, genero=?, telefono=?, direccion=?, email=?, fecha_ingreso=?, fecha_salida=?, tipo_contrato=?, estado=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getCargoId());
            ps.setString(2, entity.getCodigoEmpleado());
            ps.setString(3, entity.getNombres());
            ps.setString(4, entity.getApellidos());
            ps.setString(5, entity.getCedula());
            ps.setString(6, entity.getGenero());
            ps.setString(7, entity.getTelefono());
            ps.setString(8, entity.getDireccion());
            ps.setString(9, entity.getEmail());
            ps.setDate(10, entity.getFechaIngreso());
            ps.setDate(11, entity.getFechaSalida());
            ps.setString(12, entity.getTipoContrato());
            ps.setString(13, entity.getEstado());
            ps.setInt(14, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Personal> findById(int id) throws SQLException {
        String sql = "SELECT * FROM empleados WHERE id = ?";
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
    public List<Personal> findAll() throws SQLException {
        String sql = "SELECT * FROM empleados ORDER BY id";
        List<Personal> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM empleados";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getLong(1);
            }
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM empleados WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Personal> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM empleados ORDER BY id LIMIT ? OFFSET ?";
        List<Personal> list = new ArrayList<>();
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
