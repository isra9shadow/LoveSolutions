package Paq;

import Auxiliar.Constantes;
import static Auxiliar.Constantes.rol;
import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;
import javax.swing.JOptionPane;

/**
 *
 * @author isra9
 */
public class ConexionEstatica {

    //********************* Atributos *************************
    private static java.sql.Connection Conex;
    //Atributo a través del cual hacemos la conexión física.
    private static java.sql.Statement Sentencia_SQL;
    //Atributo que nos permite ejecutar una sentencia SQL
    private static java.sql.ResultSet Conj_Registros;

    public static void nueva() {
        try {
            //Cargar el driver/controlador
            String controlador = "com.mysql.jdbc.Driver";
            //String controlador = "oracle.jdbc.driver.OracleDriver";
            //String controlador = "sun.jdbc.odbc.JdbcOdbcDriver"; 
            //String controlador = "org.mariadb.jdbc.Driver"; // MariaDB la version libre de MySQL (requiere incluir la librería jar correspondiente).
            //Class.forName(controlador).newInstance();
            Class.forName(controlador);

            String URL_BD = "jdbc:mysql://localhost/" + Constantes.BBDD;
            //String URL_BD = "jdbc:mariadb://"+this.servidor+":"+this.puerto+"/"+this.bbdd+"";
            //String URL_BD = "jdbc:oracle:oci:@REPASO";
            //String URL_BD = "jdbc:oracle:oci:@REPASO";
            //String URL_BD = "jdbc:odbc:REPASO";

            //Realizamos la conexión a una BD con un usuario y una clave.
            Conex = java.sql.DriverManager.getConnection(URL_BD, Constantes.usuario, Constantes.password);
            Sentencia_SQL = Conex.createStatement();
            System.out.println("Conexion realizada con éxito");
        } catch (Exception e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }

    public static void cerrarBD() {
        try {
            // resultado.close();
            Conex.close();
            System.out.println("Desconectado de la Base de Datos"); // Opcional para seguridad
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error de Desconexion", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static boolean UsuarioHabilitado(String DNI) {
        boolean EstaHabilitado = false;
        try {
            String sentencia = "SELECT * FROM " + Constantes.usuarios + " WHERE DNI='" + DNI + "' AND UsuHabilitado='1'";
            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);
            if (ConexionEstatica.Conj_Registros.next()) {
                EstaHabilitado = true;
            }
        } catch (SQLException ex) {
            System.out.println("Error en el acceso a la BD.");
        }

        return EstaHabilitado;
    }

    public static Usuario obtenerUsuario(String DNI) {
        Usuario u2 = null;
        System.out.println("entramos en obtener");
        try {
            String sentencia = "SELECT * FROM " + Constantes.usuarios + " WHERE DNI = '" + DNI + "'";
            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);
            if (ConexionEstatica.Conj_Registros.next()) //Si devuelve true es que existe.
            {

                String email = Conj_Registros.getString("Email");
                String nick = Conj_Registros.getString("nick");
                String sexo = Conj_Registros.getString("Sexo");
                Date fechaNacimiento = Conj_Registros.getDate("FechaNac");
                u2 = new Usuario(DNI, nick, email, sexo, fechaNacimiento);

            }
            System.out.println(u2);
        } catch (SQLException ex) {
            System.out.println("Error en la obtencin de usuario de la BD.");
        }
        return u2;
    }

    public static LinkedList obtenerMensajes(String dni1, String dni2) {
        LinkedList<Mensaje> Mensajes = new LinkedList<Mensaje>();
        Mensaje m;
        try {
            String sentencia = "SELECT * FROM " + Constantes.mensajes + " WHERE ( Emisor='" + dni1 + "' AND Receptor='" + dni2 + "') OR ( Emisor='" + dni2 + "' AND Receptor='" + dni1 + "')";
            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);
            while (ConexionEstatica.Conj_Registros.next()) //Si devuelve true es que existe.
            {
                int id = Conj_Registros.getInt("ID");
                String Emisor = Conj_Registros.getString("Emisor");
                String Receptor = Conj_Registros.getString("Receptor");
                String Mensaje = Conj_Registros.getString("Mensaje");
                m = new Mensaje(id,Emisor, Receptor, Mensaje);
                Mensajes.add(m);
            }
        } catch (SQLException ex) {
            System.out.println("Error en la obtencion de mensajes en la BD.");
        }
        return Mensajes;
    }

    public static void leerMensaje(int id,String DNI) {
        try {
            String Sentencia = "UPDATE " + Constantes.mensajes + " SET Leido = '1' WHERE ID = '" + id + "' AND Receptor='"+DNI+"'";
            ConexionEstatica.Sentencia_SQL.executeUpdate(Sentencia);
        } catch (SQLException ex) {
            System.out.println("Error en el marcado de leido en  BD.");
        }

    }

    public static boolean insertarMensaje(Mensaje m) {
        boolean estaEnviado = false;
        try {
            String sentencia = "INSERT INTO " + Constantes.mensajes + " (`Emisor`, `Receptor`, `Mensaje`) VALUES ('" + m.getEmisor() + "','" + m.getReceptor() + "','" + m.getMensaje() + "')";
            ConexionEstatica.Sentencia_SQL.executeUpdate(sentencia);
            estaEnviado = true;

        } catch (SQLException ex) {
            System.out.println("Error de insercion de mensaje en la BD.");

        }
        return estaEnviado;
    }

    public static boolean ExisteDNI(String DNI) {
        Boolean ExisteDNI = false;
        try {
            String sentencia = "SELECT * FROM " + Constantes.usuarios + " WHERE DNI='" + DNI + "'";
            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);
            if (ConexionEstatica.Conj_Registros.next()) //Si devuelve true es que existe.
            {
                ExisteDNI = true;

            }
        } catch (SQLException ex) {
            System.out.println("Error en la busqueda DNI a la BD.");
        }

        return ExisteDNI;
    }

    public static int ObtenerRol(String DNI) {
        int rol = 0;
        try {
            String sentencia = "SELECT * FROM " + Constantes.rol + " WHERE DNI = '" + DNI + "'";

            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);

            if (ConexionEstatica.Conj_Registros.next()) //Si devuelve true es que existe.
            {

                rol = ConexionEstatica.Conj_Registros.getInt("ID_R");
            }
        } catch (SQLException ex) {
            System.out.println("Error al obtener rol de la BD.");
        }
        return rol;
    }

    public static Usuario Login(String correo, String pass) {
        Usuario u = null;
        try {
            String sentencia = "SELECT * FROM " + Constantes.usuarios + " WHERE Email = '" + correo + "' AND Pass= '" + pass + "'";
            ConexionEstatica.Conj_Registros = ConexionEstatica.Sentencia_SQL.executeQuery(sentencia);
            if (ConexionEstatica.Conj_Registros.next()) //Si devuelve true es que existe.
            {
                String DNI = Conj_Registros.getString("DNI");
                String email = Conj_Registros.getString("Email");
                String nick = Conj_Registros.getString("nick");
                String sexo = Conj_Registros.getString("Sexo");
                Date fechaNacimiento = Conj_Registros.getDate("FechaNac");
                u = new Usuario(DNI, nick, email, sexo, fechaNacimiento);

            }
        } catch (SQLException ex) {
            System.out.println("Error en el login a la BD.");
        }
        return u;
    }

    public static void CrearUsuario(Usuario u, String p) {
        try {

            String sentencia = "INSERT INTO " + Constantes.usuarios + " VALUES ('" + u.getDNI() + "','" + u.getNick() + "','" + u.getEmail() + "','" + p + "','" + u.getSexo() + "',default,default)";
            ConexionEstatica.Sentencia_SQL.executeUpdate(sentencia);
            System.out.println("Insertado correctamente");
        } catch (SQLException ex) {
            System.out.println("Error en la creacion de usuario en  BD.");
        }

    }
    //----------------------------------------------------------

    public void Modificar_Dato(String tabla, String DNI, String Nuevo_Nombre) throws SQLException {
        String Sentencia = "UPDATE " + tabla + " SET Nombre = '" + Nuevo_Nombre + "' WHERE DNI = '" + DNI + "'";
        ConexionEstatica.Sentencia_SQL.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public void Insertar_Dato(String tabla, String DNI, String Nombre, int Tfno) throws SQLException {
        String Sentencia = "INSERT INTO " + tabla + " VALUES ('" + DNI + "'," + "'" + Nombre + "','" + Tfno + "')";
        ConexionEstatica.Sentencia_SQL.executeUpdate(Sentencia);
    }

    //----------------------------------------------------------
    public void Borrar_Dato(String tabla, String DNI) throws SQLException {
        String Sentencia = "DELETE FROM " + tabla + " WHERE DNI = '" + DNI + "'";
        ConexionEstatica.Sentencia_SQL.executeUpdate(Sentencia);
    }

}
