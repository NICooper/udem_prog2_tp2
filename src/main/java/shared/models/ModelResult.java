package shared.models;

import java.io.Serializable;

/**
 * Une représentation du résultat d'un appel à un modèle. Les champs sont publics car c'est destiné à être consommé
 * immédiatement par le code qui reçoit ce résultat et ne fait pas parti de l'état persistant de l'application.
 * @param <T> Le type d'objet contenu par ce ModelResult.
 */
public class ModelResult<T> implements Serializable {
    /**
     * Les données retournées par le modèle.
     */
    public T data;

    /**
     * True si l'opération à réussi, false sinon.
     */
    public boolean success;

    /**
     * Un court message explicant pourquoi l'opération n'a pas réussi.
     */
    public String message;

    /**
     * Crée un nouveau ModelResult avec data = null, success = false, et message égale au String vide.
     */
    public ModelResult() {
        this(null, false, "");
    }

    /**
     * Crée un nouveau ModelResult.
     * @param data Les données retournées par le modèle.
     * @param success Boolean qui représente le succès de l'opération.
     * @param message Message explicant pourquoi l'opération n'a pas réussi.
     */
    public ModelResult(T data, boolean success, String message) {
        this.data = data;
        this.success = success;
        this.message = message;
    }
}
