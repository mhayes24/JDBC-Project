// Mike Hayes
// G#01165321

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class cs450_part2_GUI extends Application
{

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
        primaryStage.setTitle("CS450 Project Part 2");
        primaryStage.initStyle(StageStyle.DECORATED);
        PasswordField passField = new PasswordField();
        Button button = new Button("Enter");
        HBox hBox = new HBox(passField, button);
        hBox.setAlignment(Pos.CENTER);

        Label label = new Label("Enter Manager Password");
        VBox all = new VBox();
        all.getChildren().addAll(label, hBox);
        all.setAlignment(Pos.CENTER);
        all.setSpacing(30.0);

        Scene scene = new Scene(all, 500, 250);
        primaryStage.setScene(scene);
        primaryStage.show();

        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver not found");
        }

        Set<String> managers = new HashSet<String>();

        Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu",
                "mhayes24", "sibeegee");


        String stmnt = "select Mgrssn from Department";
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery(stmnt);

        while (r.next()) {
            managers.add(r.getString(1));
        }

        EventHandler<ActionEvent> event = e -> {
            if (!managers.contains(passField.getText())) {
                Label label1 = new Label("You are not a manager");
                Button b = new Button("Exit");
                VBox v = new VBox(label1, b);
                v.setAlignment(Pos.CENTER);
                v.setSpacing(30.0);
                Scene scene1 = new Scene(v, 500, 250);
                primaryStage.setScene(scene1);

                EventHandler<ActionEvent> event1 = e1 ->
                {
                    System.exit(0);

                };

                b.setOnAction(event1);
            } else {
                String stmnt1 = "select fname, lname from Employee where ssn = '" + passField.getText() + "'";
                try {
                    Statement s1 = conn.createStatement();
                    ResultSet r1 = s1.executeQuery(stmnt1);

                    String fname = "";
                    String lname = "";
                    while (r1.next()) {
                        fname = r1.getString(1);
                        lname = r1.getString(2);
                    }

                    options(primaryStage, fname, lname);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        };
        button.setOnAction(event);
    }


    public void options(Stage stage, String fname, String lname)
    {
        Label label = new Label("Welcome " + fname + " " + lname );
        Label label1 = new Label("Please select an option");
        VBox vBox = new VBox(label, label1);
        vBox.setAlignment(Pos.CENTER);
        Button b1 = new Button("Create new Employee");
        Button b2 = new Button("Exit");
        HBox hBox1 = new HBox(b1, b2);
        hBox1.setSpacing(5.0);
        hBox1.setAlignment(Pos.CENTER);
        VBox vBox1 = new VBox(vBox,hBox1);
        vBox1.setAlignment(Pos.CENTER);
        vBox1.setSpacing(30.0);
        Scene scene = new Scene(vBox1, 500, 250);
        stage.setScene(scene);

        EventHandler<ActionEvent> event = e ->
        {
            createEmployee(stage);

        };
        EventHandler<ActionEvent> event1 = e ->
        {
            System.exit(0);
        };

        b1.setOnAction(event);
        b2.setOnAction(event1);
    }

    public Employee createEmployee(Stage stage)
    {
        Label fnameLabel = new Label("First Name");
        Label minitLabel = new Label("Middle Initial");
        Label lnameLabel = new Label("Last Name");
        Label ssnLabel = new Label("SSN");
        Label bdateLabel = new Label("Birth Date (yyyy-mm-dd)");
        Label addressLabel = new Label("Address");
        Label sexLabel = new Label("Sex");
        Label salaryLabel = new Label("Salary");
        Label superSsnLabel = new Label("Manager SSN");
        Label dnoLabel = new Label("Dept Number");
        Label depLabel = new Label("Dependents?");

        TextField fnameTxt = new TextField();
        TextField minitTxt = new TextField();
        TextField lnameTxt = new TextField();
        TextField ssnTxt = new TextField();
        TextField bdateTxt = new TextField();
        TextField addressTxt = new TextField();
        TextField sexTxt = new TextField();
        TextField salaryTxt = new TextField();
        TextField superSsnTxt = new TextField();
        TextField dnoTxt = new TextField();
        CheckBox depBox = new CheckBox();
        Button b = new Button("Submit");

        VBox labels = new VBox();
        labels.getChildren().addAll(fnameLabel, minitLabel, lnameLabel, ssnLabel, bdateLabel, addressLabel,
                sexLabel, salaryLabel, superSsnLabel, dnoLabel, depLabel, b);
        labels.setSpacing(19.0);
        VBox textFields = new VBox();
        textFields.getChildren().addAll(fnameTxt, minitTxt, lnameTxt, ssnTxt, bdateTxt, addressTxt,
                sexTxt, salaryTxt, superSsnTxt, dnoTxt, depBox);
        textFields.setSpacing(10.0);

        HBox all = new HBox();
        all.getChildren().addAll(labels, textFields);
        all.setSpacing(15.0);
        Scene scene = new Scene(all, 300, 450);
        stage.setScene(scene);

        Employee employee = new Employee();
        EventHandler<ActionEvent> event = e ->
        {
            employee.setFname(fnameTxt.getText());
            employee.setMinit(minitTxt.getText());
            employee.setLname(lnameTxt.getText());
            employee.setSSN(ssnTxt.getText());
            employee.setBdate(bdateTxt.getText());
            employee.setAddress(addressTxt.getText());
            employee.setSex(sexTxt.getText());
            employee.setSalary(Double.parseDouble(salaryTxt.getText()));
            employee.setSuperssn(superSsnTxt.getText());
            employee.setdNo(Integer.parseInt(dnoTxt.getText()));

            try
            {
                addToDB(employee);
                if(depBox.isSelected())
                {
                    addDependent(stage, employee);
                }
                else
                {
                    addProjects(stage, employee);
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        };

        b.setOnAction(event);

        return employee;
    }

    public void addToDB(Employee emp) throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu",
                "mhayes24","sibeegee");


        String stmnt = "insert into Employee (fname, minit, lname, ssn, bdate, address, sex, salary, superssn,dno)" +
                " values ('" + emp.getFname() + "', '" + emp.getMinit() + "', '" + emp.getLname() + "', '" +
                emp.getSSN() + "', " + "TO_DATE('" + emp.getBdate() + "', 'YYYY-MM-DD'), '" + emp.getAddress() + "', '"
                + emp.getSex() + "', " + emp.getSalary() + ", '" + emp.getSuperssn() + "', " + emp.getdNo() + ")";
        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery(stmnt);

    }

    public void addDependent(Stage stage, Employee emp) throws SQLException {
        Label dnameLabel = new Label("Dependent Name");
        Label sexLabel = new Label("Sex");
        Label bdateLabel = new Label("Birth Date (yyyy-mm-dd)");
        Label relationLabel = new Label("Relationship");

        TextField dnameTxt = new TextField();
        TextField sexTxt = new TextField();
        TextField bdateTxt = new TextField();
        TextField relationTxt = new TextField();
        Button b = new Button("Submit");

        VBox labels = new VBox();
        labels.getChildren().addAll(dnameLabel, sexLabel, bdateLabel, relationLabel, b);
        labels.setSpacing(19.0);

        VBox textFields = new VBox();
        textFields.getChildren().addAll(dnameTxt, sexTxt, bdateTxt, relationTxt);
        textFields.setSpacing(10.0);

        HBox all = new HBox();
        all.getChildren().addAll(labels, textFields);
        all.setSpacing(15.0);
        all.setAlignment(Pos.CENTER);
        Scene scene = new Scene(all, 300, 450);
        stage.setScene(scene);

        EventHandler<ActionEvent> event = e ->
        {
            Connection conn = null;
            try
            {
                conn = DriverManager.getConnection(
                        "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu",
                        "mhayes24", "sibeegee");


                String stmnt = "insert into Dependent (Essn, Dependent_Name, Sex, Bdate, Relationship) " +
                        "values ('" + emp.getSSN() + "','" + dnameTxt.getText() + "','" + sexTxt.getText() + "'," +
                        "TO_DATE('" + bdateTxt.getText() + "', 'YYYY-MM-DD'), '" + relationTxt.getText() + "')";

                Statement s = conn.createStatement();
                ResultSet r = s.executeQuery(stmnt);
                addProjects(stage, emp);
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        };

        b.setOnAction(event);
}

    public void addProjects(Stage stage, Employee emp) throws SQLException
    {
        Label proj = new Label("Projects");
        Label xLabel = new Label("Project X");
        Label yLabel = new Label("Project Y");
        Label zLabel = new Label("Project Z");
        Label compLabel = new Label("Computerization");
        Label reorgLabel = new Label("Reorganization");
        Label newLabel = new Label("Newbenefits");

        Label hrs= new Label("Enter Hours");
        TextField xTxt = new TextField();
        TextField yTxt = new TextField();
        TextField zTxt = new TextField();
        TextField compTxt = new TextField();
        TextField reorgTxt = new TextField();
        TextField newTxt = new TextField();
        Button b = new Button("Submit");

        VBox labels = new VBox();
        labels.getChildren().addAll(proj, xLabel, yLabel, zLabel, compLabel, reorgLabel, newLabel, b);
        labels.setSpacing(19.0);
        VBox textFields = new VBox();
        textFields.getChildren().addAll(hrs, xTxt, yTxt, zTxt, compTxt, reorgTxt, newTxt);
        textFields.setSpacing(12.0);

        HBox all = new HBox();
        all.getChildren().addAll(labels, textFields);
        all.setSpacing(15.0);
        Scene scene = new Scene(all, 300, 450);
        stage.setScene(scene);

        EventHandler<ActionEvent> event = e ->
        {
            Connection conn = null;
            try
            {

                conn = DriverManager.getConnection(
                        "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu",
                        "mhayes24", "sibeegee");
                double total = 0;
                if(!(xTxt.getText().isEmpty())){total+=Double.parseDouble(xTxt.getText());}
                if(!(yTxt.getText().isEmpty())){total+=Double.parseDouble(yTxt.getText());}
                if(!(zTxt.getText().isEmpty())){total+=Double.parseDouble(zTxt.getText());}
                if(!(compTxt.getText().isEmpty())){total+=Double.parseDouble(compTxt.getText());}
                if(!(reorgTxt.getText().isEmpty())){total+=Double.parseDouble(reorgTxt.getText());}
                if(!(newTxt.getText().isEmpty())){total+=Double.parseDouble(newTxt.getText());}

                if(total > 40.0)
                {
                    over40(stage, emp);
                }
                else {
                    if (!(xTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 1 ,'" + xTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    if (!(yTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 2 ,'" + yTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    if (!(zTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 3 ,'" + zTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    if (!(compTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 10 ,'" + compTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    if (!(reorgTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 20 ,'" + reorgTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    if (!(newTxt.getText().isEmpty())) {
                        String stmnt = "insert into Works_On (Essn, Pno, Hours) " +
                                "values ('" + emp.getSSN() + "', 30 ,'" + newTxt.getText() + "')";

                        Statement s = conn.createStatement();
                        ResultSet r = s.executeQuery(stmnt);
                    }

                    printOut(stage, emp);
                }
            }
            catch (SQLException ex)
            {
                ex.printStackTrace();
            }
        };

        b.setOnAction(event);
    }

    public void over40(Stage stage, Employee emp)
    {
        Label label = new Label("Error: Please enter 40 or less hours");
        Button b = new Button("Go Back");
        VBox v = new VBox(label, b);
        label.setAlignment(Pos.CENTER);
        b.setAlignment(Pos.CENTER);
        v.setAlignment(Pos.CENTER);
        v.setSpacing(30.0);

        Scene scene = new Scene(v, 500, 250);
        stage.setScene(scene);

        EventHandler<ActionEvent> event = e ->
        {
            try {
                addProjects(stage, emp);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };
        b.setOnAction(event);
    }
    public void printOut(Stage stage, Employee emp) throws SQLException {

        String fname = "", minit = "", lname = "", ssn = "", bdate = "", address = "", sex = "", superssn = "";
        int dno = 0;
        double salary = 0;

        Connection conn = DriverManager.getConnection(
                "jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu",
                "mhayes24","sibeegee");

        String stmnt = "select Fname, minit, Lname, SSN, bdate, address," +
                "sex, salary, superssn, dno from EMPLOYEE where ssn = '" + emp.getSSN() + "'";

        Statement s = conn.createStatement();
        ResultSet r = s.executeQuery(stmnt);

        while (r.next())
        {
            fname = r.getString(1);
            minit = r.getString(2);
            lname = r.getString(3);
            ssn = r.getString(4);
            bdate = r.getString(5);
            address = r.getString(6);
            sex = r.getString(7);
            salary = r.getDouble(8);
            superssn = r.getString(9);
            dno = r.getInt(10);
        }




        Label label = new Label("Employee: ");
        HBox hbox = new HBox(label);
        hbox.setAlignment(Pos.CENTER);

        Label fnameLabel = new Label("First Name: " + fname);
        Label minitLabel = new Label("Middle Initial: " + minit);
        Label lnameLabel = new Label("Last Name: " + lname);
        Label ssnLabel = new Label("SSN: " + ssn);
        Label bdateLabel = new Label("Birth Date: " + emp.getBdate());
        Label addressLabel = new Label("Address: " + address);
        Label sexLabel = new Label("Sex: " + sex);
        Label salaryLabel = new Label("Salary: " + salary);
        Label superSsnLabel = new Label("Manager SSN: " + superssn);
        Label dnoLabel = new Label("Dept Number: " + dno);

        Button b = new Button("exit");
        b.setAlignment(Pos.CENTER);

        HBox hBox1 = new HBox();
        hBox1.getChildren().addAll(fnameLabel, minitLabel, lnameLabel, ssnLabel, bdateLabel);
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(10.0);

        HBox hbox2 = new HBox();
        hbox2.getChildren().addAll(addressLabel, sexLabel, salaryLabel, superSsnLabel, dnoLabel);
        hbox2.setAlignment(Pos.CENTER);
        hbox2.setSpacing(10.0);

        VBox all = new VBox();
        all.getChildren().addAll(hbox, hBox1, hbox2, b);
        all.setAlignment(Pos.CENTER);
        all.setSpacing(20.0);
        Scene scene = new Scene(all, 700, 250);
        stage.setScene(scene);
        stage.show();

        EventHandler<ActionEvent> event = e ->
        {
            System.exit(0);

        };

        b.setOnAction(event);
    }

}
