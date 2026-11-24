import assessments.AssessmentFeed;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static assessments.AssessmentFeed.*;
import static javax.swing.text.StyleConstants.setBackground;

public class Main {
    private static class Student {
        final String name;
        final String CID;
        final String type;
        
//        PROGRAMMING_TEST = "Independent Programming Test";
//        String AI_PROJECT = "AI-Assisted Programming Project";
        
        final String[] examName = {PROGRAMMING_TEST, AI_PROJECT};
        final String[] subExamName = {PROGRAM_MARK, EXPLAINED_SUCCESSFULLY, FUNCTIONALITY_MARK, VERIFICATION_MARK};
        
        final double[] marks = {};
        final double[] subMarks = {};
        
        final double[] combinedExams = {};
        
        final double[] weightsModule = {0.3, 0.7};
        final double[] weightedExams = {};
        
        double totalGrade = 0;
        double aiDiff;
        boolean ai;
        
//        int busyUntil = 0; // Every service starts free.
        
        Student(String name, String CID, String type) {
            this.name = name;
            this.CID = CID;
            this.type = type;
        }
        
        boolean setMarks(){
            try {
//                can upgrade to for loop if time
                marks[0] = getResult(CID, PROGRAMMING_TEST, PROGRAM_MARK);
                subMarks[0] = getResult(CID, PROGRAMMING_TEST, EXPLAINED_SUCCESSFULLY);
                marks[1] = getResult(CID, AI_PROJECT, FUNCTIONALITY_MARK);
                subMarks[1] = getResult(CID, AI_PROJECT, VERIFICATION_MARK);
                return true;
            }
            catch (Exception IllegalArgumentException) {
                return false;
            }
        }
        
        void calculateScores(){
//            finding marks for Indpeendant exam condition
            if (subMarks[0] == 0) {
                if (marks[0] >= 40) {combinedExams[0] = 40;}
                else {combinedExams[0] = marks[0];}
            }
            else {combinedExams[0] = marks[0];}
            
//            finding marks for ai assisited exam
            combinedExams[1] = 0.4*marks[1] + 0.6* subMarks[1];
            
//            finding the weighted exams and the total grade
            for (int i =0; i<weightsModule.length; i++){
                weightedExams[i] = weightsModule[i]*combinedExams[i];
                totalGrade += weightedExams[i];
            }
            
//            finding the diff between ai and independent and flagging it if necessary
            aiDiff = combinedExams[1] - combinedExams[0];
            ai = aiDiff >= 20;
        }
    }
    private static class mscStudent extends Student {
//        private final String name;
//        private final String CID;
//        private final String type;

//        PROGRAMMING_TEST = "Independent Programming Test";
//        String AI_PROJECT = "AI-Assisted Programming Project";
//        String DESIGN_VIVA = "Software Design Viva";
        
        final String[] examName = {PROGRAMMING_TEST, AI_PROJECT, DESIGN_VIVA};
        final String[] subExamName = {PROGRAM_MARK, EXPLAINED_SUCCESSFULLY, FUNCTIONALITY_MARK, VERIFICATION_MARK, DESIGN_MARK, EXPLANATION_MARK};
        
        final double[] marks = {};
        private final double[] subMarks = {};
        
        final double[] combinedExams = {};
        
        final double[] weightsModule = {0.25, 0.45, 0.3};
        final double[] weightedExams = {};
        
        double totalGrade = 0;
        double aiDiff;
        boolean ai;

//        int busyUntil = 0; // Every service starts free.
        
        mscStudent(String name, String CID, String type) {
            super(name, CID, type);
//            this.name = name;
//            this.CID = CID;
//            this.type = type;
        }
        
        @Override
        boolean setMarks(){
            try {
//                can upgrade to for loop if time
                marks[0] = getResult(CID, PROGRAMMING_TEST, PROGRAM_MARK);
                subMarks[0] = getResult(CID, PROGRAMMING_TEST, EXPLAINED_SUCCESSFULLY);
                marks[1] = getResult(CID, AI_PROJECT, FUNCTIONALITY_MARK);
                subMarks[1] = getResult(CID, AI_PROJECT, VERIFICATION_MARK);
                marks[2] = getResult(CID, DESIGN_VIVA, DESIGN_MARK);
                subMarks[2] = getResult(CID, DESIGN_VIVA, EXPLANATION_MARK);
                return true;
            }
            catch (Exception IllegalArgumentException) {
                return false;
            }
        }
        
        @Override
        void calculateScores(){
//            finding marks for Indpeendant exam condition
            if (subMarks[0] == 0) {
                if (marks[0] >= 40) {combinedExams[0] = 40;}
                else {combinedExams[0] = marks[0];}
            }
            else {combinedExams[0] = marks[0];}

//            finding marks for ai assisited exam
            combinedExams[1] = 0.4*marks[1] + 0.6* subMarks[1];
            
//            finding for Software Design
            combinedExams[2] = 0.5* marks[2] + 0.5*subMarks[2];

//            finding the weighted exams and the total grade
            for (int i =0; i<weightsModule.length; i++){
                weightedExams[i] = weightsModule[i]*combinedExams[i];
                totalGrade += weightedExams[i];
            }

//            finding the diff between ai and independent and flagging it if necessary
//            ideally each exam would have a flag permitting ai usage or not,
//            it would then average through the ones with and wihtout and take diff. i don't have time.
            aiDiff = combinedExams[1] - ((combinedExams[0] + combinedExams[2])/2);
            ai = aiDiff >= 20;
        }
    }
    
    public static void main(String[] args) {
        // Give Swing the job of creating the window on its GUI thread.
        SwingUtilities.invokeLater(() -> createWindow());
    }
    private static void createWindow() {
        String[] students = getCIDs().toArray(new String[0]);
//        System.out.print(Arrays.toString(students));
        
        JPanel grid = new JPanel(new GridLayout(students.length, 0, 8, 8));
        JLabel[] labels = new JLabel[students.length];
        ArrayList<Student> studentVar = new ArrayList<>();
        
        for (int s = 0; s < students.length; s++){
            String type = getStudentType(students[s]);
            if (Objects.equals(type, "UG")) {
                studentVar.add(new Student(AssessmentFeed.getStudentName(students[s]), students[s], "UG"));}
            else {studentVar.add(new mscStudent(AssessmentFeed.getStudentName(students[s]), students[s], "MSc"));}
            
            
            labels[s] = new JLabel();
            labels[s].setBorder(BorderFactory.createEtchedBorder());
            
            String text = "<html><b>" + studentVar.get(s).name + "</b>";
            text += " - " + studentVar.get(s).CID + " - " + studentVar.get(s).type + " - ";
            
            for (int i =0; i < studentVar.get(s).examName.length; i++) {
                text += (String)studentVar.get(s).examName[i] + ": " + studentVar.get(s).weightedExams[i] + " - ";
            }
            
            text += "Weighted Percentage Module Mark: " + studentVar.get(s).totalGrade;
            
            // The label is already in the grid. Swing redraws its new text.
            labels[s].setText(text + "</html>");
            
            if (studentVar.get(s).ai) {
                labels[s].setOpaque(true);
                labels[s].setBackground(Color.RED);
            }
//            labels[s].setBackground(studentVar.get(s).ai ? Color.RED : Color.WHITE);
            
            grid.add(labels[s]);
            
            text += "<br> AI DISCREPANCY:" + studentVar.get(s).ai;
            
            for (int i = 0; i < studentVar.get(s).subExamName.length; i++){
                text += studentVar.get(s).subExamName[i] + ": ";
                if (i % 2 == 0) {text += studentVar.get(s).marks[i] + "<br>";}
                else {text += studentVar.get(s).subMarks[i] + "<br>";}
            }
            text += "REPORT CONCLUDED";
            System.out.println(text);
        }
        
        JFrame frame = new JFrame("Students");
        frame.add(grid);
        frame.setSize(600, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
}