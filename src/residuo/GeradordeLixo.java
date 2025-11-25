package residuo;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Thread produtora (Gerador de Lixo).
 * Gera resíduos aleatoriamente e os coloca na fila de coleta.
 */
public class GeradordeLixo implements Runnable {

    // Tipos possíveis de resíduos
    private TipoResiduo[] tipoResiduo;

    // Fila compartilhada onde o lixo será depositado
    private final BlockingQueue<Residuos> filaColeta;

    // Nome da thread (apenas para debug/log se quiser)
    private final String nomeGerador;

    // Controle de execução
    private volatile boolean ativo = true;

    private final Random random = new Random();

    /**
     * Construtor usado na simulação.
     *
     * @param nomeGerador nome da thread geradora (ex.: "Gerador-1")
     * @param filaColeta  fila compartilhada onde serão colocados os resíduos
     */
    public GeradordeLixo(String nomeGerador, BlockingQueue<Residuos> filaColeta) {
        this.tipoResiduo = TipoResiduo.values();
        this.nomeGerador = nomeGerador;
        this.filaColeta = filaColeta;
    }

    /**
     * Construtor padrão mantido apenas para compatibilidade.
     * (Não é usado no fluxo principal.)
     */
    public GeradordeLixo() {
        this("Gerador-unico", null);
    }

    /**
     * Pede para a thread parar de gerar lixo.
     */
    public void parar() {
        ativo = false;
    }

    @Override
    public void run() {
        // Enquanto o gerador estiver ativo, cria resíduos aleatórios
        while (ativo) {
            try {
                // Escolhe um tipo de resíduo aleatório
                TipoResiduo tipo = tipoResiduo[random.nextInt(tipoResiduo.length)];

                // Cria o resíduo e coloca na fila de coleta (se a fila estiver configurada)
                if (filaColeta != null) {
                    filaColeta.put(new Residuos(tipo));
                }

                // Pequena pausa para simular o tempo entre gerações de lixo
                // (entre 200 ms e 1 s)
                Thread.sleep(200 + random.nextInt(800));

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public TipoResiduo[] getTipoResiduo() {
        return tipoResiduo;
    }

    public void setTipoResiduo(TipoResiduo[] tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }
}
