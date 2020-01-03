package repository;

import java.util.Set;

import domain.exception.ClienteException;
import domain.model.Cliente;
import domain.model.Municipio;

public interface ClienteDAO {
	
	void inserir(Cliente model) throws ClienteException;

	void alterar(Cliente model) throws ClienteException;

	void apagar(Cliente model) throws ClienteException;

	Set<Cliente> selecionar(Municipio municipio) throws ClienteException;

}
