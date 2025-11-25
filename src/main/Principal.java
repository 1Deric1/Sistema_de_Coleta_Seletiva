package main;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import residuo.ColetorLixo;
import residuo.GeradordeLixo;
import residuo.Residuos;
import residuo.TipoResiduo;

/**
 * Classe principal da aplicação.
 * Controla o tempo da simulação (3 minutos), inicia as threads
 * geradoras e a thread coletora e, ao final, exibe as estatísticas.
 */
public class Principal {

    public static void main(String[] args) {

        // Duração total da simulação em milissegundos (3 minutos)
        final long DURACAO_SIMULACAO_MS = 3 * 60 * 1000L;
        // Quantidade de threads geradoras de lixo
        final int NUM_GERADORES = 3;

        // Fila de coleta compartilhada entre geradores e coletor
        BlockingQueue<Residuos> filaColeta = new LinkedBlockingQueue<>();

        // Cria e inicia as threads geradoras
        GeradordeLixo[] geradores = new GeradordeLixo[NUM_GERADORES];
        Thread[] threadsGeradores = new Thread[NUM_GERADORES];

        for (int i = 0; i < NUM_GERADORES; i++) {
            geradores[i] = new GeradordeLixo("Gerador-" + (i + 1), filaColeta);
            threadsGeradores[i] = new Thread(geradores[i]);
            threadsGeradores[i].start();
        }

        // Cria e inicia a thread coletora
        ColetorLixo coletor = new ColetorLixo(filaColeta);
        Thread threadColetor = new Thread(coletor);
        threadColetor.start();

        long inicioSimulacao = System.currentTimeMillis();

        // Deixa a simulação rodando pelo tempo configurado
        try {
            Thread.sleep(DURACAO_SIMULACAO_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Pede para os geradores pararem
        for (GeradordeLixo gerador : geradores) {
            gerador.parar();
        }

        // Aguarda o término das threads geradoras
        for (Thread t : threadsGeradores) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Pede para o coletor finalizar (ele ainda processa o que restar na fila)
        coletor.parar();

        try {
            threadColetor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long fimSimulacao = System.currentTimeMillis();

        // ============ IMPRESSÃO DOS RESULTADOS ============
        System.out.println("===== RESULTADOS DA SIMULACAO =====");
        System.out.println("Tempo total de simulacao: "
                + ((fimSimulacao - inicioSimulacao) / 1000) + " segundos");
        System.out.println("Tempo total de coleta (processando itens): "
                + coletor.getTempoTotalColeta() + " ms");

        int totalReciclaveis = coletor.getReciclaveis().size();
        int totalNaoReciclaveis = coletor.getNaoReciclaveis().size();
        int totalResiduos = totalReciclaveis + totalNaoReciclaveis;

        System.out.println("\nQuantidade gerada por tipo (3 minutos):");
        for (TipoResiduo tipo : TipoResiduo.values()) {
            Map<TipoResiduo, Integer> mapa = coletor.getContagemPorTipo();
            int qtd = mapa.getOrDefault(tipo, 0);
            System.out.printf("- %s: %d%n", tipo.name(), qtd);
        }

        System.out.println("\nTotal reciclados: " + totalReciclaveis);
        System.out.println("Total NAO reciclaveis: " + totalNaoReciclaveis);

        double taxaReciclagem = totalResiduos == 0
                ? 0
                : (totalReciclaveis * 100.0) / totalResiduos;

        System.out.printf("\nTaxa de reciclagem: %.2f%%%n", taxaReciclagem);

        System.out.println("\nAnalise ambiental automatica:");
        if (taxaReciclagem >= 60.0) {
            System.out.println("-> Simulacao SUSTENTAVEL. A maioria dos residuos foi reciclada.");
        } else if (taxaReciclagem >= 40.0) {
            System.out.println("-> Simulacao REGULAR. Ha espaco para melhorar a separacao.");
        } else {
            System.out.println("-> Simulacao NAO SUSTENTAVEL. Poucos residuos foram reciclados.");
        }

        System.out.println("===================================");
    }
}
