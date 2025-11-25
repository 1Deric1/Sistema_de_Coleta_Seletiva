package residuo;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Thread consumidora (Coletor de Lixo).
 * Retira resíduos da fila de coleta, separa em recicláveis e não recicláveis,
 * e contabiliza as estatísticas da simulação.
 */
public class ColetorLixo implements Runnable {

    private final BlockingQueue<Residuos> filaColeta;

    // Armazéns finais
    private final List<Residuos> reciclaveis = new ArrayList<>();
    private final List<Residuos> naoReciclaveis = new ArrayList<>();

    // Contagem de resíduos por tipo
    private final Map<TipoResiduo, Integer> contagemPorTipo =
            new EnumMap<>(TipoResiduo.class);

    // Controle de execução
    private volatile boolean ativo = true;

    // Tempo total gasto processando os itens (em ms)
    private long tempoTotalColeta = 0;

    public ColetorLixo(BlockingQueue<Residuos> filaColeta) {
        this.filaColeta = filaColeta;
    }

    /**
     * Pede para o coletor finalizar.
     * Ele ainda processa o que restar na fila antes de sair.
     */
    public void parar() {
        ativo = false;
    }

    @Override
    public void run() {
        // Enquanto o coletor estiver ativo ou ainda existir algo na fila
        while (ativo || !filaColeta.isEmpty()) {
            try {
                // Espera até 500 ms por um resíduo na fila
                Residuos residuo = filaColeta.poll(500, TimeUnit.MILLISECONDS);

                if (residuo != null) {
                    long inicio = System.currentTimeMillis();

                    // Separa em reciclável e não reciclável
                    if (residuo.getTipoResiduo().isE_Reciclavel()) {
                        reciclaveis.add(residuo);
                    } else {
                        naoReciclaveis.add(residuo);
                    }

                    // Atualiza contagem por tipo
                    contagemPorTipo.merge(residuo.getTipoResiduo(), 1, Integer::sum);

                    long fim = System.currentTimeMillis();
                    tempoTotalColeta += (fim - inicio);
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public List<Residuos> getReciclaveis() {
        return reciclaveis;
    }

    public List<Residuos> getNaoReciclaveis() {
        return naoReciclaveis;
    }

    public Map<TipoResiduo, Integer> getContagemPorTipo() {
        return contagemPorTipo;
    }

    public long getTempoTotalColeta() {
        return tempoTotalColeta;
    }
}
