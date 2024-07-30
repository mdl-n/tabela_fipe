package ProjetoTabelaFipe.TabelaFipe.service;

import java.util.List;

public interface IObterDados {

    <T> T obterDados (String json, Class<T> classe);

    <T>List<T> obterLista(String json, Class<T> classe);
}
