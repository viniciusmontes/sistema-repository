package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import domain.exception.MunicipioException;
import domain.model.Municipio;
import domain.model.UFVO;
import repository.MunicipioDAO;

public class JdbcMunicipioDAO implements MunicipioDAO {

	@Override
	public void inserir(Municipio model) throws MunicipioException {
		Connection c;
		String sql;
		PreparedStatement query;

		try {
			// Abrir conexão
			c = Datasource.openConnection();
			// Escrever SQL
			sql = "INSERT INTO municipio (nm_municipio, id_uf) VALUES(?, ?)";

			try {
				// Criar uma consulta
				query = c.prepareStatement(sql);
				// Preparar a consulta
				query.setString(1, model.getNome());
				query.setString(2, model.getUf().toString());
				// Executar a consulta
				query.executeUpdate();
			} catch (SQLException cause) { // Tratar as exceptions
				String m = cause.getErrorCode() == 1062 ? "Município duplicado!" : "PROBLEMAS AO INSERIR MUNICÍPIO!";
				throw new MunicipioException(m, cause);
			} finally {
				// Fechar a conexão
				Datasource.close(c);
			}
		} catch (DataBaseException cause) {
			throw new MunicipioException(cause);
		}
	}

	@Override
	public void alterar(Municipio model) throws MunicipioException {
		// Escrever SQL
		String sql = "UPDATE municipio " + "SET nm_municipio = ?, id_uf = ? WHERE id_municipio = ?";
		// Abrir conexão e criar uma consulta
		// JDK 7 (try-with-resources)
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setString(1, model.getNome());
			q.setString(2, model.getUf().toString());
			q.setInt(3, model.getId());
			// Executar a consulta
			q.executeUpdate();
		} catch (SQLException cause) {
			throw new MunicipioException("PROBLEMAS AO ALTERAR MUNICÍPIO!", cause);
		} catch (DataBaseException cause) {
			// Tratar as exceptions
			throw new MunicipioException(cause);
		}
	}

	@Override
	public void apagar(Municipio model) throws MunicipioException {
		// Escrever SQL
		String sql = "DELETE FROM municipio WHERE id_municipio = ?";
		// Abrir conexão e criar uma consulta
		// JDK 7 (try-with-resources)
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setInt(1, model.getId());
			// Executar a consulta
			q.executeUpdate();
		} catch (SQLException cause) {
			throw new MunicipioException("PROBLEMAS AO APAGAR MUNICÍPIO!", cause);
		} catch (DataBaseException cause) {
			// Tratar as exceptions
			throw new MunicipioException(cause);
		}
	}

	@Override
	public Set<Municipio> selecionar(UFVO uf) throws MunicipioException {
		String sql = "SELECT id_municipio, nm_municipio " + "FROM municipio WHERE id_uf = ?";
		Set<Municipio> municipios = new HashSet<>();
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setString(1, uf.toString());
			// Executar a consulta
			ResultSet rs = q.executeQuery();
			Municipio m;
			while (rs.next()) {
				m = new Municipio();

				m.setId(rs.getInt("id_municipio"));
				m.setNome(rs.getString("nm_municipio"));
				m.setUf(uf);

				municipios.add(m);
			}
			return municipios;
		} catch (SQLException cause) {
			throw new MunicipioException("PROBLEMAS AO SELECIONAR MUNICÍPIOS DE " + uf, cause);
		} catch (DataBaseException cause) {
			throw new MunicipioException(cause);
		}
	}

}
