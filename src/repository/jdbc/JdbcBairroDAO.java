package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.HashSet;
import java.util.Set;

import domain.exception.BairroException;
import domain.model.Bairro;
import domain.model.Municipio;
import domain.model.UFVO;
import repository.BairroDAO;

public class JdbcBairroDAO implements BairroDAO {

	@Override
	public void inserir(Bairro model) throws BairroException {
		Connection c;
		String sql;
		PreparedStatement query;

		try {
			// Abrir conexão
			c = Datasource.openConnection();
			// Escrever SQL
			sql = "INSERT INTO bairro (nm_bairro, nm_municipio, id_uf) VALUES(?, ?, ?)";

			try {
				// Criar uma consulta
				query = c.prepareStatement(sql);
				// Preparar a consulta

				query.setString(1, model.getNome());
				query.setString(2, model.getMunicipio().toString());
				query.setString(3, model.getUf().toString());

				// Executar a consulta
				query.executeUpdate();
			} catch (SQLException cause) { // Tratar as exceptions
				String b = cause.getErrorCode() == 1062 ? "Bairro duplicado!" : "PROBLEMAS AO INSERIR BAIRRO!";
				throw new BairroException(b, cause);
			} finally {
				// Fechar a conexão
				Datasource.close(c);
			}
		} catch (DataBaseException cause) {
			throw new BairroException(cause);
		}
	}

	@Override
	public void alterar(Bairro model) throws BairroException {
		// Escrever SQL
		String sql = "UPDATE bairro " + "SET nm_bairro = ?, nm_municipio = ? WHERE id_bairro = ?";
		// Abrir conexão e criar uma consulta
		// JDK 7 (try-with-resources)
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setString(1, model.getNome());
			q.setString(2, model.getMunicipio().toString());
			// Executar a consulta
			q.executeUpdate();
		} catch (SQLException cause) {
			throw new BairroException("PROBLEMAS AO ALTERAR BAIRRO!", cause);
		} catch (DataBaseException cause) {
			// Tratar as exceptions
			throw new BairroException(cause);
		}
	}

	@Override
	public void apagar(Bairro model) throws BairroException {
		// Escrever SQL
		String sql = "DELETE FROM bairro WHERE id_bairro = ?";
		// Abrir conexão e criar uma consulta
		// JDK 7 (try-with-resources)
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setInt(1, model.getId());
			// Executar a consulta
			q.executeUpdate();
		} catch (SQLException cause) {
			throw new BairroException("PROBLEMAS AO APAGAR BAIRRO!", cause);
		} catch (DataBaseException cause) {
			// Tratar as exceptions
			throw new BairroException(cause);
		}
	}

	@Override
	public Set<Bairro> selecionar(Municipio municipio, UFVO uf) throws BairroException {
		String sql = "SELECT id_bairro, nm_bairro, nm_municipio, id_uf " + "FROM bairro WHERE nm_municipio = ?";
		Set<Bairro> bairros = new HashSet<>();
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setString(1, municipio.toString());
			// Executar a consulta
			ResultSet rs = q.executeQuery();
			Bairro b;
			while (rs.next()) {

				b = new Bairro();

				b.setId(rs.getInt("id_bairro"));
				b.setNome(rs.getString("nm_bairro"));
				b.setMunicipio(municipio);
				b.setUf(uf);

				bairros.add(b);
			}
			return bairros;
		} catch (SQLException cause) {
			throw new BairroException("PROBLEMAS AO SELECIONAR BAIRROS DE " + municipio, cause);
		} catch (DataBaseException cause) {
			throw new BairroException(cause);
		}

	}

}
