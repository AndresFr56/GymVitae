package gym.vitae.repositories;

import gym.vitae.model.Inscripciones;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscripcionesRepository extends Repository<Inscripciones> {

    private Inscripciones mapRow(ResultSet rs) throws SQLException {
        Inscripciones i = new Inscripciones();
        i.setId(rs.getInt("id"));
        i.setHorarioId(rs.getInt("horario_id"));
        i.setClienteId(rs.getInt("cliente_id"));
        i.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
        i.setEstado(rs.getString("estado"));
        i.setCreatedAt(rs.getTimestamp("created_at"));
        i.setUpdatedAt(rs.getTimestamp("updated_at"));
        return i;
    }

    @Override
    public Inscripciones save(Inscripciones entity) throws SQLException {
        // Try to use stored procedure sp_inscribir_clase
        String call = "{ CALL sp_inscribir_clase(?, ?) }";
        try (Connection conn = getConnection(); CallableStatement cs = conn.prepareCall(call)) {
            cs.setInt(1, entity.getHorarioId());
            cs.setInt(2, entity.getClienteId());
            cs.execute();
            // Try to find inserted row
            String lookup = "SELECT id, fecha_inscripcion " +
                    "FROM inscripciones_clases " +
                    "WHERE horario_id=? AND cliente_id=? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(lookup)) {
                ps.setInt(1, entity.getHorarioId());
                ps.setInt(2, entity.getClienteId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        entity.setId(rs.getInt("id"));
                        entity.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
                    }
                }
            }
            return entity;
        }
    }

    @Override
    public boolean update(Inscripciones entity) throws SQLException {
        String sql = "UPDATE inscripciones_clases " +
                "SET horario_id=?, cliente_id=?, fecha_inscripcion=?, estado=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getHorarioId());
            ps.setInt(2, entity.getClienteId());
            ps.setDate(3, entity.getFechaInscripcion());
            ps.setString(4, entity.getEstado());
            ps.setInt(5, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM inscripciones_clases WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Inscripciones> findById(int id) throws SQLException {
        String sql = "SELECT * FROM inscripciones_clases WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Inscripciones> findAll() throws SQLException {
        String sql = "SELECT * FROM inscripciones_clases ORDER BY id";
        List<Inscripciones> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM inscripciones_clases";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM inscripciones_clases WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Inscripciones> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM inscripciones_clases ORDER BY id LIMIT ? OFFSET ?";
        List<Inscripciones> list = new ArrayList<>();
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
