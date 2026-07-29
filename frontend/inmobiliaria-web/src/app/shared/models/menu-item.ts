export interface MenuItem {

  /**
   * Identificador único del menú.
   */
  id?: string;

  /**
   * Texto que verá el usuario.
   */
  titulo: string;

  /**
   * Icono de Angular Material.
   */
  icono: string;

  /**
   * Ruta del router.
   */
  ruta: string;

  /**
   * Grupo al que pertenece.
   */
  grupo: string;

  /**
   * Roles que pueden visualizar la opción.
   */
  roles?: string[];

  /**
   * Orden dentro del grupo.
   */
  orden?: number;

  /**
   * Permite ocultar opciones sin eliminarlas.
   */
  visible?: boolean;

  /**
   * Badge opcional.
   * Ejemplo:
   * "3"
   * "Nuevo"
   */
  badge?: string;

  /**
   * Hijos para futuros submenús.
   */
  hijos?: MenuItem[];

}
