package ProjetoTabelaFipe.TabelaFipe.principal;

import ProjetoTabelaFipe.TabelaFipe.model.Dados;
import ProjetoTabelaFipe.TabelaFipe.model.Modelos;
import ProjetoTabelaFipe.TabelaFipe.model.Veiculo;
import ProjetoTabelaFipe.TabelaFipe.service.ConsumoApi;
import ProjetoTabelaFipe.TabelaFipe.service.ConverterDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    Scanner scanner = new Scanner(System.in);
    String endereco;
    ConsumoApi consumoApi = new ConsumoApi();
    ConverterDados conversor = new ConverterDados();
    final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";
    public void exibirMenu() {
        var menu = """
                Escreva a opcao de veiculo que deseja:
                Carro
                Moto
                Caminhao
                """;
        System.out.println(menu);
        String leitura = scanner.nextLine();
        if(leitura.contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        } else if(leitura.contains("carr")){
            endereco = URL_BASE + "carros/marcas";
        } else {
            endereco = URL_BASE + "caminhoes/marcas";
        }

        var json = consumoApi.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream().sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("\nAgora, informe pelo codigo uma marca que deseja:");
       leitura = scanner.nextLine();

        endereco += "/" + leitura + "/modelos";
        json = consumoApi.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo)).forEach(System.out::println);

        System.out.println("\nDigite um trecho do veiculo desejado: ");
        var leituraTrecho = scanner.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos()
                .stream().filter(m -> m.nome().toLowerCase().contains(leituraTrecho.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("Modelos filtrados: ");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o codigo do veiculo desejado:");
        var codigomodelo = scanner.nextLine();

        endereco += "/" + codigomodelo + "/anos";
        json = consumoApi.obterDados(endereco);
        List<Dados> anos =  conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoApi.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\n todos veiculos filtrados com o valor:");
        veiculos.forEach(System.out::println);



    }
}