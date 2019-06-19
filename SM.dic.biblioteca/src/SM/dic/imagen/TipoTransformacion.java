package SM.dic.imagen;

/**
 * Tipos de transformaciones de tipo filtro y contraste que se pueden realizar sobre una imagen
 * @author David Infante Casas
 */
public enum TipoTransformacion {
    
    //Filtro
    /**
     * Filtro Media
     */
    MEDIA,

    /**
     * Filtro Binomial
     */
    BINOMIAL,

    /**
     * Filtro Enfoque
     */
    ENFOQUE,

    /**
     * Filtro Relieve
     */
    RELIEVE,

    /**
     * Filtro Laplaciano
     */
    LAPLACIANO,

    /**
     * Ningún filtro
     */
    NINGUNO,

    //Contraste
    /**
     * Contraste
     */
    CONTRASTE,

    /**
     * Iluminado
     */
    ILUMINAR,

    /**
     * Oscurecido
     */
    OSCURECER,

    /**
     * Negativo
     */
    NEGATIVO

}
