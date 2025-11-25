package residuo;

/**
 * Classe que representa um resíduo individual.
 * Ele apenas guarda o tipo de resíduo.
 */
public class Residuos {

    private TipoResiduo tipoResiduo;

    public Residuos(TipoResiduo tipoResiduo) {
        this.tipoResiduo = tipoResiduo;
    }

    public TipoResiduo getTipoResiduo() {
        return tipoResiduo;
    }

    @Override
    public String toString() {
        // Representação textual simples do resíduo
        return "Resíduo: " + tipoResiduo.name();
    }
}
