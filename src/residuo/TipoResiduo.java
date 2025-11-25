package residuo;

/**
 * Enum que representa os tipos de resíduos do sistema.
 * Cada tipo já vem marcado se é reciclável ou não reciclável.
 */
public enum TipoResiduo {

    PAPEL(true, false),
    PLASTICO(true, false),
    VIDRO(true, false),
    METAL(true, false),
    ORGANICO(false, true),
    NAO_RECICLAVEL(false, true); // tipo adicional pedido no enunciado

    // atributos para saber o tipo de residuo
    private boolean nao_Reciclavel;
    private boolean e_Reciclavel;

    // Construtor do enum
    TipoResiduo(boolean e_Reciclavel, boolean nao_Reciclavel) {
        this.e_Reciclavel = e_Reciclavel;
        this.nao_Reciclavel = nao_Reciclavel;
    }

    public boolean isNao_Reciclavel() {
        return nao_Reciclavel;
    }

    public boolean isE_Reciclavel() {
        return e_Reciclavel;
    }

    // Setters mantidos apenas para compatibilidade com o desenho original.
    // Não são usados na simulação.
    public void setNao_Reciclavel(boolean nao_Reciclavel) {
        this.nao_Reciclavel = nao_Reciclavel;
    }

    public void setE_Reciclavel(boolean e_Reciclavel) {
        this.e_Reciclavel = e_Reciclavel;
    }
}
