package gym.vitae.repositories;

import gym.vitae.model.Nominas;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NominaRepository extends Repository<Nominas>{

    private Nominas mapRow(ResultSet rs) throws SQLException {
        Nominas n = new Nominas();
        n.setId(rs.getInt("id"));
        n.setEmpleadoId(rs.getInt("empleado_id"));
        n.setMes(rs.getInt("mes"));
        n.setAnio(rs.getInt("anio"));
        n.setSalarioBase(rs.getBigDecimal("salario_base"));
        n.setBonificaciones(rs.getBigDecimal("bonificaciones"));
        n.setDeducciones(rs.getBigDecimal("deducciones"));
        n.setHorasExtra(rs.getBigDecimal("horas_extra"));
        n.setValorHoraExtra(rs.getBigDecimal("valor_hora_extra"));
        n.setTotalPagar(rs.getBigDecimal("total_pagar"));
        n.setFechaPago(rs.getDate("fecha_pago"));
        n.setEstado(rs.getString("estado"));
        n.setObservaciones(rs.getString("observaciones"));
        n.setGeneradaPor(rs.getInt("generada_por"));
        n.setAprobadaPor(rs.getInt("aprobada_por"));
        n.setPagadaPor(rs.getInt("pagada_por"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        n.setUpdatedAt(rs.getTimestamp("updated_at"));
        return n;
    }

    @Override
    public Nominas save(Nominas entity) throws SQLException {
        // Try stored procedure sp_generar_nominas_mes if appropriate (fallback to single insert)
        String insert = "INSERT INTO nominas " +
                "(empleado_id, mes, anio, salario_base, bonificaciones, deducciones, horas_extra, valor_hora_extra, total_pagar, fecha_pago, estado, observaciones, generada_por, aprobada_por, pagada_por) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = getConnection()) {
            // If id is zero, do insert
            try (PreparedStatement ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, entity.getEmpleadoId());
                ps.setInt(2, entity.getMes());
                ps.setInt(3, entity.getAnio());
                ps.setBigDecimal(4, entity.getSalarioBase());
                ps.setBigDecimal(5, entity.getBonificaciones());
                ps.setBigDecimal(6, entity.getDeducciones());
                ps.setBigDecimal(7, entity.getHorasExtra());
                ps.setBigDecimal(8, entity.getValorHoraExtra());
                ps.setBigDecimal(9, entity.getTotalPagar());
                ps.setDate(10, entity.getFechaPago());
                ps.setString(11, entity.getEstado());
                ps.setString(12, entity.getObservaciones());

                if (entity.getGeneradaPor() != null) {
                    ps.setInt(13, entity.getGeneradaPor());
                }
                else ps.setNull(13, Types.INTEGER);

                if (entity.getAprobadaPor() != null) {
                    ps.setInt(14, entity.getAprobadaPor());
                }
                else ps.setNull(14, Types.INTEGER);
                if (entity.getPagadaPor() != null) {
                    ps.setInt(15, entity.getPagadaPor());
                }
                else ps.setNull(15, Types.INTEGER);

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) entity.setId(rs.getInt(1));
                }
                return entity;
            }
        }
    }

    @Override
    public boolean update(Nominas entity) throws SQLException {
        String sql = "UPDATE nominas " +
                "SET empleado_id=?, mes=?, anio=?, salario_base=?, bonificaciones=?, deducciones=?, horas_extra=?, valor_hora_extra=?, total_pagar=?, fecha_pago=?, estado=?, observaciones=?, generada_por=?, aprobada_por=?, pagada_por=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, entity.getEmpleadoId());
            ps.setInt(2, entity.getMes());
            ps.setInt(3, entity.getAnio());
            ps.setBigDecimal(4, entity.getSalarioBase());
            ps.setBigDecimal(5, entity.getBonificaciones());
            ps.setBigDecimal(6, entity.getDeducciones());
            ps.setBigDecimal(7, entity.getHorasExtra());
            ps.setBigDecimal(8, entity.getValorHoraExtra());
            ps.setBigDecimal(9, entity.getTotalPagar());
            ps.setDate(10, entity.getFechaPago());
            ps.setString(11, entity.getEstado());
            ps.setString(12, entity.getObservaciones());

            if (entity.getGeneradaPor() != null) {
                ps.setInt(13, entity.getGeneradaPor());
            }else ps.setNull(13, Types.INTEGER);

            if (entity.getAprobadaPor() != null) {
                ps.setInt(14, entity.getAprobadaPor());
            }else ps.setNull(14, Types.INTEGER);

            if (entity.getPagadaPor() != null) {
                ps.setInt(15, entity.getPagadaPor());
            }else ps.setNull(15, Types.INTEGER);

            ps.setInt(16, entity.getId());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM nominas WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Optional<Nominas> findById(int id) throws SQLException {
        String sql = "SELECT * FROM nominas WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Nominas> findAll() throws SQLException {
        String sql = "SELECT * FROM nominas ORDER BY id";
        List<Nominas> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    @Override
    public long count() throws SQLException {
        String sql = "SELECT COUNT(*) FROM nominas";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        }
        return 0;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT 1 FROM nominas WHERE id = ? LIMIT 1";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public List<Nominas> findAll(int offset, int limit) throws SQLException {
        String sql = "SELECT * FROM nominas ORDER BY id LIMIT ? OFFSET ?";
        List<Nominas> list = new ArrayList<>();
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
