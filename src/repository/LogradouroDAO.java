package repository;

import java.util.Set;

import domain.exception.LogradouroException;
import domain.model.Bairro;
import domain.model.Logradouro;
import domain.model.Municipio;
import domain.model.UFVO;

public interface LogradouroDAO {
	
	void inserir(Logradouro model) throws LogradouroException;
	
	void alterar(Logradouro model) throws LogradouroException;
	
	void apagar(Logradouro model) throws LogradouroException;
	
	Set<Logradouro> selecionar (UFVO uf, Municipio municipio, Bairro bairro) throws LogradouroException;

}
