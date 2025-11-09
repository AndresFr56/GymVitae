package gym.vitae;

import gym.vitae.controller.EmpleadosController;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym_vitaePU"); // usa tu persistence-unit
        EntityManager em = emf.createEntityManager();

        EmpleadosController empleadosController = new EmpleadosController();
        System.out.println("Lista de empleados:");
        empleadosController.getEmpleados().forEach(empleado ->
                System.out.println(empleado.getNombres() + " " + empleado.getApellidos()));



    }
}
