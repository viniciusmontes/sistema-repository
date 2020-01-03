package repository;

import java.util.Set;

import domain.exception.MunicipioException;
import domain.model.Municipio;
import domain.model.UFVO;

public interface MunicipioDAO {

	void inserir(Municipio model) throws MunicipioException;

	void alterar(Municipio model) throws MunicipioException;

	void apagar(Municipio model) throws MunicipioException;

	Set<Municipio> selecionar(UFVO uf) throws MunicipioException;

}
