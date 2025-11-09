package gym.vitae;

import gym.vitae.controller.EmpleadosController;
import gym.vitae.core.ApplicationConfig;
import gym.vitae.model.enums.EstadoCliente;

public class Main {

  public static void main(String[] args) {

    ApplicationConfig.init();

    EmpleadosController empleadosController = new EmpleadosController();
    System.out.println("Lista de empleados:");
    empleadosController
        .getEmpleados()
        .forEach(
            empleado -> System.out.println(empleado.getNombres() + " " + empleado.getApellidos()));

    System.out.println(EstadoCliente.ACTIVO);
  }
}
