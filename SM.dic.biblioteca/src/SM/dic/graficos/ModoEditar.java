package SM.dic.graficos;

/**
 * Tipo de edición sobre las formas del lienzo
 * @author David Infante Casas
 */
public enum ModoEditar {

    /**
     * No se está editando
     */
    NULL,

    /**
     * Se están moviendo las formas
     */
    MOVER,

    /**
     * Se están borrando formas
     */
    BORRAR,

    /**
     * Se están seleccionando formas
     */
    SELECCIONAR
}
