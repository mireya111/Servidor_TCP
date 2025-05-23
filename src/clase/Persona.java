package clase;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Persona implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter @Setter int id;
    @Getter @Setter String nombre;
    @Getter @Setter String horaIngreso;
    @Getter @Setter String horaSalidaA;
    @Getter @Setter String horaEntradaA;
    @Getter @Setter String horaSalida;

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", horaIngreso='" + horaIngreso + '\'' +
                ", horaSalidaA='" + horaSalidaA + '\'' +
                ", horaEntradaA='" + horaEntradaA + '\'' +
                ", horaSalida='" + horaSalida + '\'' +
                '}';
    }

    public Persona(int id, String nombre, String horaIngreso, String horaSalidaA, String horaEntradaA, String horaSalida) {
        this.id = id;
        this.nombre = nombre;
        this.horaIngreso = horaIngreso;
        this.horaSalidaA = horaSalidaA;
        this.horaEntradaA = horaEntradaA;
        this.horaSalida = horaSalida;
    }
    //Variable para guardar a las personas que se registran por el día actual
    public static final Map<String, Persona> personasTimbran = new HashMap<>();
    //Funciòn para colocar los ides de los empleados
    private static final Map<String, Persona> personasPredefinidas = new HashMap<>();
    public static void ingresarPersonasQuemadas() {
        Persona p1 = new Persona(123, "Juan Perez", null, null, null, null);
        Persona p2 = new Persona(234, "Maria Lopez", null, null, null, null);
        Persona p3 = new Persona(345, "Carlos Garcia", null, null, null, null);
        Persona p4 = new Persona(456, "Ana Martinez", null, null, null, null);
        Persona p5 = new Persona(567, "Luis Rodriguez", null, null, null, null);
        Persona p6 = new Persona(678, "Laura Hernandez", null, null, null, null);
        Persona p7 = new Persona(789, "Pedro Gonzalez", null, null, null, null);
        Persona p8 = new Persona(890, "Sofia Ramirez", null, null, null, null);
        Persona p9 = new Persona(901, "Diego Torres", null, null, null, null);
        Persona p10 = new Persona(103, "Valeria Castro", null, null, null, null );

        //Agregar los empleados a la lista
        personasPredefinidas.put(p1.getNombre(), p1);
        personasPredefinidas.put(p2.getNombre(), p2);
        personasPredefinidas.put(p3.getNombre(), p3);
        personasPredefinidas.put(p4.getNombre(), p4);
        personasPredefinidas.put(p5.getNombre(), p5);
        personasPredefinidas.put(p6.getNombre(), p6);
        personasPredefinidas.put(p7.getNombre(), p7);
        personasPredefinidas.put(p8.getNombre(), p8);
        personasPredefinidas.put(p9.getNombre(), p9);
        personasPredefinidas.put(p10.getNombre(), p10);
    }

    //Funciòn para buscar segun el id del empleado enviado por el cliente
    public static Persona buscarPersona(String nombre, String jornada, String hora) {
        Persona persona = personasPredefinidas.get(nombre);
        if (persona == null) {
            System.out.println("Empleado no encontrado");
            return null;
        }
        Persona personaTimbrada = personasTimbran.get(nombre);
        if (personaTimbrada == null) {
            personaTimbrada= new Persona(persona.getId(), persona.getNombre(), null, null, null, null);
        }
        if (jornada.equals("Ingreso al trabajo")) {
            personaTimbrada.setHoraIngreso(hora);
        } else if (jornada.equals("Salida al Almuerzo")){
            personaTimbrada.setHoraSalidaA(hora);
        } else if (jornada.equals("Ingreso del Almuerzo")) {
            personaTimbrada.setHoraEntradaA(hora);
        } else if (jornada.equals("Salida del trabajo")) {
            personaTimbrada.setHoraSalida(hora);
        }
        //Agregar la persona a la lista de personas que timbran
        personasTimbran.put(nombre, personaTimbrada);
        return personaTimbrada;
    }
}
