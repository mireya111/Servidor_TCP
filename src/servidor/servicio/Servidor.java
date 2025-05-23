package servidor.servicio;

import clase.Persona;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Servidor {

    public static String getFecha(String nombre, String jornada){
        Date date = new Date();
        DateFormat formato = new SimpleDateFormat("HH:mm:ss");
        String horaActual = formato.format(date);
        // Obtener la persona registrada
        Persona personaEncontrada = Persona.personasTimbran.get(nombre);

        // Validar el orden de las jornadas
        if (personaEncontrada == null) {
            // Si la persona no tiene registros, debe iniciar con "Ingreso al trabajo"
            if (!jornada.equals("Ingreso al trabajo")) {
                return "<html>" +
                        "<br>Error: Debe registrar primero la Hora de Ingreso al trabajo."+"</html>";
            }
        } else {
            // Verificar si la jornada ya fue registrada
            if (jornada.equals("Ingreso al trabajo") && personaEncontrada.getHoraIngreso() != null) {
                return "<html><br>Error: La Hora de Ingreso ya ha sido registrada.</html>";
            }
            if (jornada.equals("Salida al Almuerzo") && personaEncontrada.getHoraSalidaA() != null) {
                return "<html><br>Error: La Hora de Salida al Almuerzo ya ha sido registrada.</html>";
            }
            if (jornada.equals("Ingreso del Almuerzo") && personaEncontrada.getHoraEntradaA() != null) {
                return "<html><br>Error: La Hora de Entrada del Almuerzo ya ha sido registrada.</html>";
            }
            if (jornada.equals("Salida del trabajo") && personaEncontrada.getHoraSalida() != null) {
                return "<html><br>Error: La Hora de Salida ya ha sido registrada.</html>";
            }

            // Validar el orden de las jornadas para una persona con registros previos
            if (jornada.equals("Salida al Almuerzo") && personaEncontrada.getHoraIngreso() == null) {
                return "<html>" +
                        "<br>Error: Debe registrar primero la Hora de Ingreso."+"</html>";
            }
            if (jornada.equals("Ingreso del Almuerzo") && personaEncontrada.getHoraSalidaA() == null) {
                return "<html>" +
                        "<br>Error: Debe registrar primero la Hora de Salida al Almuerzo."+"</html>";
            }
            if (jornada.equals("Salida del trabajo") && personaEncontrada.getHoraIngreso() == null) {
                return "<html>" +
                        "Error: Debe registrar primero la Hora de Entrada del Almuerzo."+"</html>";
            }
        }

        //Seteaar el objeto persona con la hora de ingreso
        Persona.buscarPersona(nombre, jornada, horaActual);
        System.out.println(Persona.personasTimbran.get(nombre));
        //return nombre + jornada + "Hora de Ingreso registrada en el Servidor " + formato.format(date);
        return "<html>" + "Datos de la asistencia" +
                "<br>Nombre del empleado: " + nombre +
                "<br>Jornada en la que se timbra:" + jornada +
                "<br>Hora de Ingreso registrada en el Servidor " + formato.format(date) + "</html>";
    }
    public void escribir (Map<String, Persona> lista, String ruta) throws Exception{
        FileOutputStream fos = new FileOutputStream(ruta);
        ObjectOutputStream oos = new  ObjectOutputStream(fos);
        oos.writeObject(lista);
        oos.close();
        System.out.println("Archivo creado exitosamente");
    }
    public Map<String, Persona> leer (String ruta) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(ruta);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Map<String, Persona> lista = (Map<String, Persona>) ois.readObject();
        ois.close();
        return lista;
    }
    //Procesar solicitud
    public static void procesarSolicitud(int puerto) throws Exception{
        //Se crean objetos con todos los empleados
        Persona.ingresarPersonasQuemadas();

        ServerSocket servidor = new ServerSocket(puerto);
        System.out.println("Servidor de fecha corriendo ....");

        while(true){
            Socket cliente = servidor.accept();
            System.out.println("CLiente conectado");

            //Se crea un flujo de entrada y salida para el cliente
            InputStream in = cliente.getInputStream();
            OutputStream out = cliente.getOutputStream();

            //Leer el nombre del empleado
            DataInputStream dis = new DataInputStream(in);
            String nombre = dis.readUTF();
            if (nombre.equals("x")) {
                cliente.close();
                break;
            }
            String jornada = dis.readUTF();
            System.out.println("Jornada recibida:" + jornada);
            String resultado = Servidor.getFecha(nombre, jornada);
            System.out.println("Mensaje recibido exitosamente");
            //Devolver la respuesta al cliente
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(resultado);
            cliente.close();
        }
        servidor.close();

        //Al finalizar se crea un archivo con los datos de los empleados
        //Se crea un objeto de la clase "Servidor" para poder usar los metodos de leer y escribir archivo
        Servidor servidor1 = new Servidor();
        //Datos requeridos para el nombre del documento
        // Datos requeridos para el nombre del documento
        String fechaDeHoy = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String rutaArchivo = "C:\\Users\\USER\\IdeaProjects\\" + fechaDeHoy + ".dat";

        // Se crea el archivo
        Map<String, Persona> registrosFinales = new HashMap<>();
        for (Map.Entry<String, Persona> entry : Persona.personasTimbran.entrySet()) {
            Persona p = entry.getValue();
            if (p.getHoraIngreso() != null && p.getHoraSalida() != null) {
                registrosFinales.put(entry.getKey(), p);
            }
        }
        if (!registrosFinales.isEmpty()) {
            try {
                servidor1.escribir(registrosFinales, rutaArchivo);
                System.out.println("Archivo creado exitosamente en: " + rutaArchivo);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error al escribir el archivo.");
            }
        } else {
            System.out.println("No hay registros para guardar.");
        }
        //Se leen los datos del archivo
        Map<String, Persona> lista = servidor1.leer(rutaArchivo);
        //Contador para saber cuantas personas se han registrado
        int contador = 0;
        //Se imprimen los datos del archivo
        for (Map.Entry<String, Persona> entry : lista.entrySet()) {
            contador++;
            Persona p = entry.getValue();
            System.out.println("Asistencia" + contador + ":");
            System.out.println("ID del empleado: " + p.getId());
            System.out.println("Nombre: " + p.getNombre());
            System.out.println("Hora de ingreso: " + p.getHoraIngreso());
            System.out.println("Hora de salida al almuerzo: " + p.getHoraSalidaA());
            System.out.println("Hora de entrada al almuerzo: " + p.getHoraEntradaA());
            System.out.println("Hora de salida: " + p.getHoraSalida());
        }

    }
}
