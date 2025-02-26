package tn.esprit.models;
import java.sql.Date;

public class Employee extends User {
    protected int idEmployee;

    public  Employee() {}

    public Employee(String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int idEmployee) {
        super(numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.idEmployee = idEmployee;
    }

    public Employee(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password, int idEmployee) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
        this.idEmployee = idEmployee;
    }
    public Employee(int id, String numTel, int joursOuvrables, String nom, String prenom, String address, String email, String gender, Date dateDeNaissance, String userType, String password) {
        super(id, numTel, joursOuvrables, nom, prenom, address, email, gender, dateDeNaissance, userType, password);
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(int idEmployee) {
        this.idEmployee = idEmployee;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "idEmployee=" + idEmployee +
                ", id=" + id +
                ", numTel='" + numTel + '\'' +
                ", joursOuvrables=" + joursOuvrables +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateDeNaissance=" + dateDeNaissance +
                ", userType='" + userType + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
