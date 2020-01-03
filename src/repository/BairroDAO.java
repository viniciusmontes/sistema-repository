package repository;

import java.util.Set;

import domain.exception.BairroException;
import domain.model.Bairro;
import domain.model.Municipio;
import domain.model.UFVO;

public interface BairroDAO {

	void inserir(Bairro model) throws BairroException;

	void alterar(Bairro model) throws BairroException;

	void apagar(Bairro model) throws BairroException;

	Set<Bairro> selecionar(Municipio municipio, UFVO uf) throws BairroException;

}
