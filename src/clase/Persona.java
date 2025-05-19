package clase;

import lombok.Getter;
import lombok.Setter;

public class Persona {
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

}
