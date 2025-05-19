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
import java.util.List;

public class Servidor {

    public static String getFecha(String nombre, String jornada){
        Date date = new Date();
        DateFormat formato = new SimpleDateFormat("HH:mm:ss");
        return nombre + jornada + "Hora de Ingreso registrada en el Servidor " + formato.format(date);
    }
    public void escribir (List<Persona> lista, String ruta) throws Exception{
        FileOutputStream fos = new FileOutputStream(ruta);
        ObjectOutputStream oos = new  ObjectOutputStream(fos);
        oos.writeObject(lista);
        oos.close();
        System.out.println("Archivo creado exitosamente");
    }
    public List <Persona> leer (String ruta) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(ruta);
        ObjectInputStream ois = new ObjectInputStream(fis);
        List <Persona> lista = (List <Persona>) ois.readObject();
        ois.close();
        return lista;
    }
    //Procesar solicitud
    public static void procesarSolicitud(int puerto) throws Exception{
        ServerSocket servidor = new ServerSocket(puerto);
        System.out.println("Servidor de fecha corriendo ....");

        while(true){
            Socket cliente = servidor.accept();
            System.out.println("CLiente conectado");
            InputStream in = cliente.getInputStream();
            OutputStream out = cliente.getOutputStream();

            //Leer el nombre del empleado
            DataInputStream dis = new DataInputStream(in);
            String nombre = dis.readUTF();
            if (nombre.equals("x")) break;
            String jornada = dis.readUTF();
            String resultado = Servidor.getFecha(nombre, jornada);
            System.out.println("Mensaje recibido exitosamente");
            //Devolver la respuesta al cliente
            DataOutputStream dos = new DataOutputStream(out);
            dos.writeUTF(resultado);
            cliente.close();

        }
        servidor.close();
    }
}
