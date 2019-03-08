/*
Thomas Lee
Spring 2017

This program presents the user with a GUI that can accept information about multiple students, including their IDs, names and majors. The user can also enter grades and their associated credits for each student. The program will calculate the GPA for each student and stores this information as well.

Use Instructions:
With the insert option selected in the drop down menu next to "Choose Selection", enter an ID, a name, a major and press Process Request.

To insert grades for a particular student, enter their ID, name and major and select the Update option from the drop down menu. This will bring up a window to enter a grade, after entering a grade, another window will appear to enter the credit amount for that particular grade/class.

To see the cumulative GPA for a student, enter the proper ID, name and major and select the Find option from the drop down menu.
 */

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public final class StudentDatabase extends JFrame {


    private Map<Integer, Student> studentMap = new HashMap<>();//MATCHES AN ID TO A STUDENT

    private JTextField idTextField = new JTextField();
    private JTextField nameTextField = new JTextField();
    private JTextField majorTextField = new JTextField();
    private JLabel idLabel = new JLabel("ID ");
    private JLabel nameLabel = new JLabel("Name ");
    private JLabel majorLabel = new JLabel("Major ");
    private JLabel chooseLabel = new JLabel("Choose Selection ");

    private JButton processButton = new JButton("Process Request ");

    private String[] comboMessages = {"Insert", "Delete", "Find", "Update"};
    private JComboBox comboBox = new JComboBox(comboMessages);

    private String[] comboGrades = {"A", "B", "C", "D", "E", "F"};
    private String[] comboCredits = {"3", "6"};

    private JComboBox comboBoxUpdate1 = new JComboBox(comboGrades);
    private JComboBox comboBoxUpdate2 = new JComboBox(comboCredits);

    //create gui and call action listeners methods
    public StudentDatabase() {

        setSize(new Dimension(400, 200));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Student GPA Database");
        setResizable(true);
        setVisible(true);

        JPanel panelMain = new JPanel(); //creating panel with default flowlayout
        getContentPane().add(panelMain); //adding panel to this

        JPanel panelForm = new JPanel(new GridBagLayout());//creating panel with gridbaglayout
        panelMain.add(panelForm);//adding form panel to main panel
        GridBagConstraints c = new GridBagConstraints();//creating grid bag constraints

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_END;

        idTextField.setPreferredSize(new Dimension(200, 20));
        nameTextField.setPreferredSize(new Dimension(200, 20));
        majorTextField.setPreferredSize(new Dimension(200, 20));

        //RIGHT SIDE OF GUI
        panelForm.add(idTextField, c);
        c.gridy++;
        panelForm.add(nameTextField, c);
        c.gridy++;
        panelForm.add(majorTextField, c);
        c.gridy++;
        panelForm.add(comboBox, c);
        c.gridy++;

        //LEFT SIDE OF GUI
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.LINE_START;
        panelForm.add(idLabel, c);
        c.gridy++;
        panelForm.add(nameLabel, c);
        c.gridy++;
        panelForm.add(majorLabel, c);
        c.gridy++;
        panelForm.add(chooseLabel, c);
        c.gridy++;
        panelForm.add(processButton, c);
        c.gridy++;

        //addEdge actionlistener to combobox
        comboBox.addActionListener(comboBox);

        //call action listener for process request button
        processRequestListener();

    }

    public void processRequestListener() {

        processButton.addActionListener(a -> {

            comboBoxMethod();  //call combobox method after process button has been clicked
        });

    }

    public void comboBoxMethod() {//this method handles the logic of the combo box selections

        //THE NEXT THREE IF STATEMENTS CHECK TO SEE IF ALL THE GUI FIELDS ARE FILLED OUT
        if (idTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(idTextField, "Please enter an ID", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (nameTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(nameTextField, "Please enter a Name", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }
        if (majorTextField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(majorTextField, "Please enter a Major", "Error", JOptionPane.ERROR_MESSAGE);
            return;

        }

        switch (comboBox.getSelectedIndex()) {
            case 0:  //insert
                isIDValid();
                if (!isIDTaken()) {
                    return;
                }
                insertNewStudentToDatabase();
                break;

            case 1://delete
                isIDValid();
                boolean checkToReturn = isIDInDatabase();
                if(!checkToReturn){
                    return;
                }

                deleteStudentFromDatabase();
                break;

            case 2://find
                isIDValid();
                boolean checkToReturn2 = isIDInDatabase();
                if(!checkToReturn2){
                    return;
                }
                findStudentInDatabase();

                break;

            case 3://update
                isIDValid();
                boolean checkToReturn3 = isIDInDatabase();
                if(!checkToReturn3){
                    return;
                }
                updateStudentsGrades();
                break;
        }
    }

    public void isIDValid() {//method to ensure ID field is a numeric value that is positive

        try {
            int idInputLocalVariable = Integer.parseInt(idTextField.getText());
            if (idInputLocalVariable < 0) {
                JOptionPane.showMessageDialog(idTextField, "Please enter a positive value", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(idTextField, "Please enter a numeric value", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public boolean isIDInDatabase() { //METHOD TO CHECK IF MAP CONTAINS THE ID REQUESTED

        if (!studentMap.containsKey(Integer.parseInt(idTextField.getText()))) {

            JOptionPane.showMessageDialog(idTextField, "No student with that ID exists in the database ", "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        } else {
            return true;
        }

    }

    private boolean isIDTaken() {//METHOD TO CHECK IF STUDENT ID ALREADY EXISTS TO PREVENT A DOUBLE INSERT

        if (studentMap.containsKey(Integer.parseInt(idTextField.getText()))) {

            JOptionPane.showMessageDialog(idTextField, "This ID already exists in the database ", "Error", JOptionPane.ERROR_MESSAGE);

            return false;
        } else {
            return true;
        }

    }

    private void insertNewStudentToDatabase() {//METHOD TO INSERT AN ID AND STUDENT INTO THE MAP

        studentMap.put(Integer.parseInt(idTextField.getText()), new Student(nameTextField.getText(), majorTextField.getText()));
        showSuccessPopup();

    }

    private void deleteStudentFromDatabase() {
        studentMap.remove(Integer.parseInt(idTextField.getText()));
        showSuccessPopup();
    }

    private void findStudentInDatabase() {//METHOD FOR FIND REQUEST, RETURNS TO STRING METHOD FROM STUDENT OBJECT VIA ID INPUT

        int findMethodKeyInt = Integer.parseInt(idTextField.getText());

        JOptionPane.showMessageDialog(idTextField, studentMap.get(findMethodKeyInt), "Student Information", JOptionPane.PLAIN_MESSAGE);
    }

    private void updateStudentsGrades() {//METHOD THAT SHOWS TWO COMBO BOXES THAT GETS A GRADE AND AMOUNT OF CREDITS
        double pointsFromBox = 0;
        double creditsFromBox = 0;

        JOptionPane.showMessageDialog(idTextField, comboBoxUpdate1, "Choose Grade ", JOptionPane.PLAIN_MESSAGE);

        int result = JOptionPane.showConfirmDialog(idTextField, //CONFIRMS GRADE, ENSURES THAT CLOSE OPERATION WORKS
                "Confirm Grade Choice",
                "Confirm Quit", JOptionPane.YES_NO_CANCEL_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(idTextField, comboBoxUpdate2, "Choose Credits ", JOptionPane.PLAIN_MESSAGE);

            JOptionPane.showConfirmDialog(idTextField,
                    "Confirm Credit Choice",//CONFIRMS GRADE, ENSURES THAT CLOSE OPERATION WORKS
                    "Confirm Quit", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.YES_OPTION) {

                switch (comboBoxUpdate1.getSelectedIndex()) {   //CONVERTING GRADE TO POINTS
                    case 0://A
                        pointsFromBox = 4.0;
                        break;
                    case 1://B
                        pointsFromBox = 3.0;
                        break;
                    case 2://C
                        pointsFromBox = 2.0;
                        break;
                    case 3://D
                        pointsFromBox = 1.0;
                        break;
                    case 4://E
                        pointsFromBox = 0.0;
                        break;
                    case 5://F
                        pointsFromBox = 0.0;
                        break;
                }

                switch (comboBoxUpdate2.getSelectedIndex()) {      //credits in the class
                    case 0://A
                        creditsFromBox = 3.0;
                        break;
                    case 1://B
                        creditsFromBox = 6.0;
                        break;

                }

                studentMap.get(Integer.parseInt(idTextField.getText())).courseCompleted(pointsFromBox, creditsFromBox);  //THIS CALL IS TO CALCUALTE THE CUMULATIVE GPA
                showSuccessPopup();
            }//end nested if
        }//end first if
    }

    private void showSuccessPopup() {
        JOptionPane.showMessageDialog(idTextField, "Success! ", "Success", JOptionPane.PLAIN_MESSAGE);

    }

    public static void main(String[] args) {

        StudentDatabase guiObj = new StudentDatabase();

    }

}

class Student {

    private double totalCredits, gradePoints, totalGradePoints, cumulativeGpa;
    private String majorInst, nameInst;

    public Student(String constructorName, String constructorMajor) {

        nameInst = constructorName;
        majorInst = constructorMajor;
        cumulativeGpa = 0.0; //DEFAULT GPA OF 0.0

    }

    public void courseCompleted(double methodGrade, double methodCredits) {

        gradePoints = methodGrade * methodCredits;
        totalCredits += methodCredits;
        totalGradePoints += gradePoints;
        cumulativeGpa = totalGradePoints / totalCredits;   //CALCULATING CUMULATIVE GPA

    }

    @Override
    public String toString() {

        return " Student Name: " + this.nameInst + " Major: " + this.majorInst + " Cumulative GPA: " + this.cumulativeGpa;

    }
}
