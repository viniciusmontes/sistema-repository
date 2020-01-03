package repository.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Set;

import domain.exception.ClienteException;
import domain.model.Cliente;
import domain.model.Municipio;
import repository.ClienteDAO;

public class JdbcClienteDAO implements ClienteDAO {

	@Override
	public void inserir(Cliente model) throws ClienteException {
		Connection c;
		String sql;
		PreparedStatement query;

		try {
			c = Datasource.openConnection();
			sql = "INSERT INTO (nm_cliente, sn_cliente, id_estado_civil, id_sexo, dt_nascimento) "
					+ "VALUES (?, ?, ?, ?, ?)";
			try {
				query = c.prepareStatement(sql);

				query.setString(1, model.getNome());
				query.setString(2, model.getSobrenome());
				query.setString(3, model.getEstadoCivil().toString());
				query.setString(4, model.getSexo().toString());
				query.setString(5, model.getDataNascimento().toString());

				query.executeUpdate();

			} catch (SQLException cause) { // Tratar as exceptions
				String e = cause.getErrorCode() == 1063 ? "Cliente duplicado!" : "PROBLEMAS AO INSERIR CLIENTE!";
				throw new ClienteException(e, cause);
			} finally {
				// Fechar a conexão
				Datasource.close(c);
			}
		} catch (DataBaseException cause) {
			throw new ClienteException(cause);
		}
	}

	@Override
	public void alterar(Cliente model) throws ClienteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void apagar(Cliente model) throws ClienteException {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<Cliente> selecionar(Municipio municipio) throws ClienteException {
		return Collections.emptySet();
	}

}
