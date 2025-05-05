package servidor.servicio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Servidor {

    public void servicio(){

        int puerto = 5000;
        try {
            DatagramSocket socket = new DatagramSocket(puerto); //Crear el datagram
            System.out.println("Servidor UDP corriendo en el puerto " + puerto + "...");

            byte[] bufferEntrada = new byte[1024]; //convertir el mensaje a cadena de bytes
            while (true) {
                // Recibir datos del cliente
                DatagramPacket paqueteEntrada = new DatagramPacket(bufferEntrada, bufferEntrada.length);
                socket.receive(paqueteEntrada);

                String mensajeRecibido = new String(paqueteEntrada.getData(), 0, paqueteEntrada.getLength());
                System.out.println("Mensaje recibido: " + mensajeRecibido);

                // Procesar el mensaje
                String[] numeros = mensajeRecibido.split(",");
                if (numeros.length == 2) {
                    try {
                        int num1 = Integer.parseInt(numeros[0].trim());
                        int num2 = Integer.parseInt(numeros[1].trim());
                        int suma = num1 + num2;
                        int resta = num1 - num2;
                        int multiplicacion = num1 * num2;
                        int dividir = num1 / num2;

                        String respuesta = "Suma: " + suma + "/n"+ "Resta: "+ resta + "/n"+"Multiplicación: "+ multiplicacion+"/n"+"Divición: "+ dividir;
                        byte[] bufferSalida = respuesta.getBytes();

                        // Enviar respuesta al cliente
                        DatagramPacket paqueteSalida = new DatagramPacket(
                                bufferSalida,
                                bufferSalida.length,
                                paqueteEntrada.getAddress(),
                                paqueteEntrada.getPort()
                        );
                        socket.send(paqueteSalida);
                        System.out.println("Respuesta enviada: " + respuesta);
                    } catch (NumberFormatException e) {
                        System.out.println("Error al convertir los números.");
                    }
                } else {
                    System.out.println("Formato de mensaje incorrecto.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
