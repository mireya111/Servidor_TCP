package servidor.test;

import servidor.servicio.Servidor;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    public static void main(String[] args) throws Exception {
        Servidor.procesarSolicitud(5000);
    }
}
