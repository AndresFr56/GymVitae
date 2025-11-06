package gym.vitae.repositories;

import gym.vitae.model.Horarios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HorariosRepository extends Repository<Horarios> {

    private Horarios mapRow(ResultSet rs) throws SQLException {
        Horarios h = new Horarios();
        h.setId(rs.getInt("id"));
        h.setClaseId(rs.getInt("clase_id"));
        h.setDiaSemana(rs.getString("dia_semana"));
        h.setHoraInicio(rs.getTime("hora_inicio"));
        h.setHoraFin(rs.getTime("hora_fin"));
        h.setInstructorId(rs.getInt("instructor_id"));
        h.setCreatedAt(rs.getTimestamp("created_at"));
        h.setUpdatedAt(rs.getTimestamp("updated_at"));
        return h;
    }

    @Override
    public Horarios save(Horarios entity) throws SQLException {
        String sql = "INSERT INTO horarios (clase_id, dia_semana, hora_inicio, hora_fin, instructor_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getClaseId() != null ? entity.getClaseId() : 0);
            ps.setString(2, entity.getDiaSemana());
            ps.setTime(3, entity.getHoraInicio());
            ps.setTime(4, entity.getHoraFin());
            if (entity.getInstructorId() != null) ps.setInt(5, entity.getInstructorId());
            else ps.setNull(5, Types.INTEGER);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Horarios entity) throws SQLException {
        String sql = "UPDATE horarios SET clase_id=?, dia_semana=?, hora_inicio=?, hora_fin=?, instructor_id=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getClaseId() != null ? entity.getClaseId() : 0);
            ps.setString(2, entity.getDiaSemana());
            ps.setTime(3, entity.getHoraInicio());
            ps.setTime(4, entity.getHoraFin());
            if (entity.getInstructorId() != null) ps.setInt(5, entity.getInstructorId());
            else ps.setNull(5, Types.INTEGER);
            ps.setInt(6, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM horarios WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Horarios> findById(int id) throws SQLException {
        String sql = "SELECT * FROM horarios WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Horarios> findAll() throws SQLException {
        String sql = "SELECT * FROM horarios ORDER BY id";
        List<Horarios> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM horarios";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM horarios WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Horarios> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM horarios ORDER BY id LIMIT ? OFFSET ?";
        List<Horarios> list = new ArrayList<>();
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
