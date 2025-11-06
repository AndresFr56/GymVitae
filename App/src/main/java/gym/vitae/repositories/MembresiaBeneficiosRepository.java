package gym.vitae.repositories;

import gym.vitae.model.MembresiaBeneficios;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembresiaBeneficiosRepository extends Repository<MembresiaBeneficios> {

    private MembresiaBeneficios mapRow(ResultSet rs) throws SQLException {
        MembresiaBeneficios mb = new MembresiaBeneficios();
        mb.setId(rs.getInt("id"));
        mb.setTipoMembresiaId(rs.getInt("tipo_membresia_id"));
        mb.setBeneficioId(rs.getInt("beneficio_id"));
        mb.setCreatedAt(rs.getTimestamp("created_at"));
        return mb;
    }

    @Override
    public MembresiaBeneficios save(MembresiaBeneficios entity) throws SQLException {
        String sql = "INSERT INTO membresia_beneficios (tipo_membresia_id, beneficio_id) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getTipoMembresiaId());
            ps.setInt(2, entity.getBeneficioId());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(MembresiaBeneficios entity) throws SQLException {
        String sql = "UPDATE membresia_beneficios SET tipo_membresia_id=?, beneficio_id=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getTipoMembresiaId());
            ps.setInt(2, entity.getBeneficioId());
            ps.setInt(3, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM membresia_beneficios WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<MembresiaBeneficios> findById(int id) throws SQLException {
        String sql = "SELECT * FROM membresia_beneficios WHERE id = ?";
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
    public List<MembresiaBeneficios> findAll() throws SQLException {
        String sql = "SELECT * FROM membresia_beneficios ORDER BY id";
        List<MembresiaBeneficios> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM membresia_beneficios";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM membresia_beneficios WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<MembresiaBeneficios> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM membresia_beneficios ORDER BY id LIMIT ? OFFSET ?";
        List<MembresiaBeneficios> list = new ArrayList<>();
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
