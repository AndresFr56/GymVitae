package gym.vitae.repositories;

import gym.vitae.model.Cargos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CargosRepository extends Repository<Cargos> implements IRepository<Cargos> {

	private Cargos mapRow(ResultSet rs) throws SQLException {
		Cargos c = new Cargos();
		c.setId(rs.getInt("id"));
		c.setNombre(rs.getString("nombre"));
		c.setSalarioBase(rs.getBigDecimal("salario_base"));
		c.setDescripcion(rs.getString("descripcion"));
		c.setActivo(rs.getBoolean("activo"));
		c.setCreatedAt(rs.getTimestamp("created_at"));
		c.setUpdatedAt(rs.getTimestamp("updated_at"));
		return c;
	}

	@Override
	public Cargos save(Cargos entity) throws SQLException {
		String sql = "INSERT INTO cargos (nombre, salario_base, descripcion, activo) VALUES (?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, entity.getNombre());
			ps.setBigDecimal(2, entity.getSalarioBase());
			ps.setString(3, entity.getDescripcion());
			ps.setBoolean(4, entity.getActivo() != null ? entity.getActivo() : true);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) entity.setId(rs.getInt(1)); }
			return entity;
		}
	}

	@Override
	public boolean update(Cargos entity) throws SQLException {
		String sql = "UPDATE cargos SET nombre=?, salario_base=?, descripcion=?, activo=? WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, entity.getNombre());
			ps.setBigDecimal(2, entity.getSalarioBase());
			ps.setString(3, entity.getDescripcion());
			ps.setBoolean(4, entity.getActivo() != null ? entity.getActivo() : true);
			ps.setInt(5, entity.getId());
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean delete(int id) throws SQLException {
		String sql = "UPDATE cargos SET activo = false WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); return ps.executeUpdate() > 0; }
	}

	@Override
	public Optional<Cargos> findById(int id) throws SQLException {
		String sql = "SELECT * FROM cargos WHERE id = ? AND activo = true";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
		}
		return Optional.empty();
	}

	@Override
	public List<Cargos> findAll() throws SQLException {
		String sql = "SELECT * FROM cargos WHERE activo = true ORDER BY id";
		List<Cargos> list = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(mapRow(rs)); }
		return list;
	}

	@Override
	public long count() throws SQLException {
		String sql = "SELECT COUNT(*) FROM cargos WHERE activo = true";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getLong(1); }
		return 0;
	}

	@Override
	public boolean existsById(int id) throws SQLException {
		String sql = "SELECT 1 FROM cargos WHERE id = ? AND activo = true LIMIT 1";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) { ps.setInt(1, id); try (ResultSet rs = ps.executeQuery()) { return rs.next(); } }
	}

	@Override
	public List<Cargos> findAll(int offset, int limit) throws SQLException {
		String sql = "SELECT * FROM cargos ORDER BY id LIMIT ? OFFSET ?";
		List<Cargos> list = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) { ps.setInt(1, limit); ps.setInt(2, offset); try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(mapRow(rs)); } }
		return list;
	}
}
