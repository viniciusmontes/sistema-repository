package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import domain.exception.LogradouroException;
import domain.model.Bairro;
import domain.model.Logradouro;
import domain.model.Municipio;
import domain.model.UFVO;
import repository.LogradouroDAO;

public class JdbcLogradouroDAO implements LogradouroDAO {

	@Override
	public void inserir(Logradouro model) throws LogradouroException {
		Connection c;
		String sql;
		PreparedStatement query;

		try {
			// Abrir conexão
			c = Datasource.openConnection();
			// Escrever SQL
			sql = "INSERT INTO logradouro (nm_logradouro, nm_municipio, id_uf, nm_bairro) VALUES(?, ?, ?, ?)";

			try {
				// Criar uma consulta
				query = c.prepareStatement(sql);
				// Preparar a consulta

				query.setString(1, model.getNome());
				query.setString(2, model.getMunicipio().toString());
				query.setString(3, model.getUf().toString());
				query.setString(4, model.getBairro().toString());
				// Executar a consulta
				query.executeUpdate();
			} catch (SQLException cause) { // Tratar as exceptions
				String b = cause.getErrorCode() == 1062 ? "Logradouro duplicado!" : "PROBLEMAS AO INSERIR Logradouro!";
				throw new LogradouroException(b, cause);
			} finally {
				// Fechar a conexão
				Datasource.close(c);
			}
		} catch (DataBaseException cause) {
			throw new LogradouroException(cause);
		}
	}

	@Override
	public void alterar(Logradouro model) throws LogradouroException {
		// TODO Auto-generated method stub

	}

	@Override
	public void apagar(Logradouro model) throws LogradouroException {
		// TODO Auto-generated method stub
		String sql = "DELETE FROM logradouro WHERE id_logradouro = ?";
		// Abrir conexão e criar uma consulta
		// JDK 7 (try-with-resources)
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setInt(1, model.getId());
			// Executar a consulta
			q.executeUpdate();
		} catch (SQLException cause) {
			throw new LogradouroException("PROBLEMAS AO APAGAR Logradouro!", cause);
		} catch (DataBaseException cause) {
			// Tratar as exceptions
			throw new LogradouroException(cause);
		}

	}

	// ** @Override
	public Set<Logradouro> selecionar(UFVO uf, Municipio municipio, Bairro bairro) throws LogradouroException {
		String sql = "SELECT id_logradouro, nm_logradouro, nm_municipio, id_uf, nm_bairro "
				+ "FROM logradouro WHERE nm_bairro = ?";
		Set<Logradouro> logradouros = new HashSet<>();
		try (Connection c = Datasource.openConnection(); PreparedStatement q = c.prepareStatement(sql)) {
			// Preparar a consulta
			q.setString(1, bairro.toString());
			// Executar a consulta
			ResultSet rs = q.executeQuery();
			Logradouro lg;
			while (rs.next()) {

				lg = new Logradouro();

				lg.setId(rs.getInt("id_logradouro"));
				lg.setNome(rs.getString("nm_logradouro"));
				lg.setMunicipio(municipio);
				lg.setUf(uf);
				lg.setBairro(bairro);

				logradouros.add(lg);
			}
			return logradouros;
		} catch (SQLException cause) {
			throw new LogradouroException("PROBLEMAS AO SELECIONAR LOGRADOUROS DE " + logradouros, cause);
		} catch (DataBaseException cause) {
			throw new LogradouroException(cause);
		}

	}

}
