package gym.vitae.repositories;

import gym.vitae.model.Membresias;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MembresiasRepository extends Repository<Membresias> {

    private Membresias mapRow(ResultSet rs) throws SQLException {
        Membresias m = new Membresias();
        m.setId(rs.getInt("id"));
        m.setClienteId(rs.getInt("cliente_id"));
        m.setTipoMembresiaId(rs.getInt("tipo_membresia_id"));
        m.setFechaInicio(rs.getDate("fecha_inicio"));
        m.setFechaFin(rs.getDate("fecha_fin"));
        m.setPrecioPagado(rs.getBigDecimal("precio_pagado"));
        m.setEstado(rs.getString("estado"));
        m.setObservaciones(rs.getString("observaciones"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        m.setUpdatedAt(rs.getTimestamp("updated_at"));
        return m;
    }

    @Override
    public Membresias save(Membresias entity) throws SQLException {
        // Prefer stored procedure sp_crear_membresia_con_factura if available
        String call = "{ CALL sp_crear_membresia_con_factura(?, ?, ?, ?, ?) }";
        try (Connection conn = getConnection(); CallableStatement cs = conn.prepareCall(call)) {
            // Assumed params: cliente_id, tipo_membresia_id, fecha_inicio, precio_pagado, empleado_responsable_id
            cs.setInt(1, entity.getClienteId());
            cs.setInt(2, entity.getTipoMembresiaId());
            cs.setDate(3, entity.getFechaInicio());
            cs.setBigDecimal(4, entity.getPrecioPagado());
            cs.setNull(5, Types.INTEGER);
            cs.execute();
            // Try to lookup last inserted for client
            String lookup = "SELECT id FROM membresias WHERE cliente_id=? ORDER BY id DESC LIMIT 1";
            try (PreparedStatement ps = conn.prepareStatement(lookup)) {
                ps.setInt(1, entity.getClienteId());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) entity.setId(rs.getInt("id"));
                }
            }
            return entity;
        } catch (SQLException ex) {
            // Fallback to direct insert
        }

        String sql = "INSERT INTO membresias " +
                "(cliente_id, tipo_membresia_id, fecha_inicio, fecha_fin, precio_pagado, estado, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, entity.getClienteId());
            ps.setInt(2, entity.getTipoMembresiaId());
            ps.setDate(3, entity.getFechaInicio());
            ps.setDate(4, entity.getFechaFin());
            ps.setBigDecimal(5, entity.getPrecioPagado());
            ps.setString(6, entity.getEstado());
            ps.setString(7, entity.getObservaciones());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) entity.setId(rs.getInt(1));
            }
            return entity;
        }
    }

    @Override
    public boolean update(Membresias entity) throws SQLException {
        String sql = "UPDATE membresias " +
                "SET cliente_id=?, tipo_membresia_id=?, fecha_inicio=?, fecha_fin=?, precio_pagado=?, estado=?, observaciones=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getClienteId());
            ps.setInt(2, entity.getTipoMembresiaId());
            ps.setDate(3, entity.getFechaInicio());
            ps.setDate(4, entity.getFechaFin());
            ps.setBigDecimal(5, entity.getPrecioPagado());
            ps.setString(6, entity.getEstado());
            ps.setString(7, entity.getObservaciones());
            ps.setInt(8, entity.getId());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM membresias WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Membresias> findById(int id) throws SQLException {
        String sql = "SELECT * FROM membresias WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Membresias> findAll() throws SQLException {
        String sql = "SELECT * FROM membresias ORDER BY id";
        List<Membresias> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM membresias";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM membresias WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Membresias> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM membresias ORDER BY id LIMIT ? OFFSET ?";
        List<Membresias> list = new ArrayList<>();
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

