package gym.vitae.repositories;

import gym.vitae.model.Clases;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClasesRepository extends Repository<Clases> implements IRepository<Clases> {

	private Clases mapRow(ResultSet rs) throws SQLException {
		Clases c = new Clases();
		c.setId(rs.getInt("id"));
		c.setNombre(rs.getString("nombre"));
		c.setDescripcion(rs.getString("descripcion"));
		c.setDuracionMinutos(rs.getInt("duracion_minutos"));
		c.setCapacidadMaxima(rs.getInt("capacidad_maxima"));
		c.setNivel(rs.getString("nivel"));
		c.setActiva(rs.getBoolean("activa"));
		c.setCreatedAt(rs.getTimestamp("created_at"));
		c.setUpdatedAt(rs.getTimestamp("updated_at"));
		return c;
	}

	@Override
	public Clases save(Clases entity) throws SQLException {
		String sql = "INSERT INTO clases (nombre, descripcion, duracion_minutos, capacidad_maxima, nivel, activa) VALUES (?, ?, ?, ?, ?, ?)";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			ps.setString(1, entity.getNombre());
			ps.setString(2, entity.getDescripcion());
			ps.setInt(3, entity.getDuracionMinutos());
			ps.setInt(4, entity.getCapacidadMaxima());
			ps.setString(5, entity.getNivel());
			ps.setBoolean(6, entity.getActiva() != null ? entity.getActiva() : true);
			ps.executeUpdate();
			try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) entity.setId(rs.getInt(1)); }
			return entity;
		}
	}

	@Override
	public boolean update(Clases entity) throws SQLException {
		String sql = "UPDATE clases SET nombre=?, descripcion=?, duracion_minutos=?, capacidad_maxima=?, nivel=?, activa=? WHERE id=?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, entity.getNombre());
			ps.setString(2, entity.getDescripcion());
			ps.setInt(3, entity.getDuracionMinutos());
			ps.setInt(4, entity.getCapacidadMaxima());
			ps.setString(5, entity.getNivel());
			ps.setBoolean(6, entity.getActiva() != null ? entity.getActiva() : true);
			ps.setInt(7, entity.getId());
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public boolean delete(int id) throws SQLException {
		String sql = "UPDATE clases SET activo = false WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			return ps.executeUpdate() > 0;
		}
	}

	@Override
	public Optional<Clases> findById(int id) throws SQLException {
		String sql = "SELECT * FROM clases WHERE id = ? AND activo = true";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, id);
			try (ResultSet rs = ps.executeQuery()) { if (rs.next()) return Optional.of(mapRow(rs)); }
		}
		return Optional.empty();
	}

	@Override
	public List<Clases> findAll() throws SQLException {
		String sql = "SELECT * FROM clases WHERE activo = true ORDER BY id";
		List<Clases> list = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
			while (rs.next()) list.add(mapRow(rs));
		}
		return list;
	}

	@Override
	public long count() throws SQLException {
		String sql = "SELECT COUNT(*) FROM clases";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) { if (rs.next()) return rs.getLong(1); }
		return 0;
	}

	@Override
	public boolean existsById(int id) throws SQLException {
		String sql = "SELECT 1 FROM clases WHERE id = ? AND activo = false LIMIT 1";
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) { ps.setInt(1, id); try (ResultSet rs = ps.executeQuery()) { return rs.next(); } }
	}

	@Override
	public List<Clases> findAll(int offset, int limit) throws SQLException {
		String sql = "SELECT * FROM clases ORDER BY id LIMIT ? OFFSET ?";
		List<Clases> list = new ArrayList<>();
		try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, limit);
			ps.setInt(2, offset);
			try (ResultSet rs = ps.executeQuery()) { while (rs.next()) list.add(mapRow(rs)); }
		}
		return list;
	}
}
