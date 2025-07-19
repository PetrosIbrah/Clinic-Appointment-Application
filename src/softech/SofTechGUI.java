package softech;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class SofTechGUI extends javax.swing.JFrame {

    static final String MYSQL_SUB = "jdbc:mysql:";
    static final String DB_NAME = "softech";
    static final String MYSQL_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_SERVER = "//localhost:3306/";
    static final String DB_USER = "root";
    static final String DB_PASS = "";
    static final String DB_URL = MYSQL_SUB + DB_SERVER + DB_NAME;
    private Connection conn;
    private static int TikCounter = 0;

    private boolean DatabaseConnection() {
        try {
            // Class.forName(MYSQL_DRIVER);
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean DatabaseDisconnection() {
        try {
            conn.close();
            return true;
        } catch (SQLException e) {
            System.err.println(e);
            return false;
        }
    }

    private boolean TableCreation() {
        try {
            Statement stmt = conn.createStatement();
            String SQL = "Create Table If Not Exists Schedule (AMKA NUMERIC(13, 0) Not Null Unique, Date DATE Not Null Unique, DoctorType VARCHAR(25))";
            stmt.executeUpdate(SQL);
        } catch (SQLException e) {
            return false;
        }

        try {
            Statement stmt = conn.createStatement();
            String SQL = "Create Table If Not Exists Patient (AMKA NUMERIC(13, 0), Name VARCHAR(100), Phone NUMERIC, History VARCHAR(150), Primary Key(AMKA))";
            stmt.executeUpdate(SQL);
            return true;
        } catch (SQLException e) {
            return false;
        }

    }

    private void Notification(String AppointmentDate) {
        String Message = "Your Appointment is in " + AppointmentDate;
        JOptionPane.showMessageDialog(null, Message, "Notification", JOptionPane.INFORMATION_MESSAGE);
    }

    private void Insert(String ID, String PatientName, String AppointmentDate, String DoctorType, String PatientPhone, String PatientHistory) {
        TableCreation();
        try {
            Statement stmt = conn.createStatement();
            String sql = "Insert Into Patient Values(" + Long.parseLong(ID) + ",'" + PatientName + "', " + Long.parseLong(PatientPhone) + ", '" + PatientHistory + "')";
            int rowsAffected = stmt.executeUpdate(sql);

            if (rowsAffected > 0) {
                System.out.println("Insert successful");
            } else {
                System.out.println("Insert Error");
            }

        } catch (SQLException | NumberFormatException e) {
            String Msg = "AMKA Already Exists\n Please Re-enter";
            JOptionPane.showMessageDialog(null, Msg, "Error", JOptionPane.ERROR_MESSAGE);

            System.err.println(e);
            TextArea.setText("");
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            String sql = "Insert Into Schedule Values(" + Long.parseLong(ID) + ", '" + AppointmentDate + "', '" + DoctorType + "')";
            int rowsAffected = stmt.executeUpdate(sql);

            if (rowsAffected > 0) {
                System.out.println("Insert successful");
            } else {
                System.out.println("Insert Error");
            }

        } catch (SQLException | NumberFormatException e) {
            try {
                Statement stmt = conn.createStatement();
                String sql = "Delete From Patient Where AMKA = '" + ID + "'";
                int RowsAffected = stmt.executeUpdate(sql);

            } catch (SQLException | NumberFormatException e2) {
                System.err.println(e2);
            }
            String Msg = "Date is already taken or wrong format.\n Please Re-enter";
            JOptionPane.showMessageDialog(null, Msg, "Error", JOptionPane.ERROR_MESSAGE);

            System.err.println(e);
            TextArea.setText("");
            return;
        }

        TextArea.setText("Appointment Saved!");

        LocalDate currentDate = LocalDate.now();
        String[] DateString = AppointmentDate.split("-");
        int[] DateNums = new int[3];
        DateNums[0] = Integer.parseInt(DateString[0]);
        DateNums[1] = Integer.parseInt(DateString[1]);
        DateNums[2] = Integer.parseInt(DateString[2]);

        LocalDate VirtualDate = LocalDate.of(DateNums[0], DateNums[1], DateNums[2]);
        long DaysDifference = ChronoUnit.DAYS.between(currentDate, VirtualDate);
        if (DaysDifference <= 7 && DaysDifference >= 0) {
            Notification(AppointmentDate);
        }
    }

    private void Update(String AppointmentDate, String ID) {
        try {
            Statement stmt = conn.createStatement();
            String SQLQuery = "Update Schedule Set Date = '" + AppointmentDate + "' Where AMKA = " + Long.parseLong(ID);
            int RowsAffected = stmt.executeUpdate(SQLQuery);

            if (RowsAffected > 0) {
                TextArea.setText("Appointment Edited Successfully.");
            } else {
                TextArea.setText("No Appointments Changed");
            }
        } catch (SQLException | NumberFormatException e) {
            String Msg = "Update Failed\n Date already taken or Amka not found\n Please Re-enter";
            JOptionPane.showMessageDialog(null, Msg, "Error", JOptionPane.ERROR_MESSAGE);
            TextArea.setText("");
            System.err.println(e);
        }
    }

    private void Detele(String ID) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "Delete From Schedule Where AMKA = '" + ID + "'";
            int RowsAffected = stmt.executeUpdate(sql);

            if (RowsAffected > 0) {
                TextArea.setText("Deletion successful");
            } else {
                TextArea.setText("Deletion Failed");
            }

        } catch (SQLException | NumberFormatException e) {
            String Msg = "Unable to Detele";
            JOptionPane.showMessageDialog(null, Msg, "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println(e);
            return;
        }

        try {
            Statement stmt = conn.createStatement();
            String sql = "Delete From Patient Where AMKA = '" + ID + "'";
            int RowsAffected = stmt.executeUpdate(sql);

            if (RowsAffected > 0) {
                TextArea.setText("Deletion successful");
            } else {
                TextArea.setText("Deletion Failed");
            }

        } catch (SQLException | NumberFormatException e) {
            String Msg = "Unable to Delete";
            JOptionPane.showMessageDialog(null, Msg, "Error", JOptionPane.ERROR_MESSAGE);
            TextArea.setText("");
            System.err.println(e);
        }
    }

    public SofTechGUI() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jLabel1 = new javax.swing.JLabel();
        Connect = new javax.swing.JCheckBox();
        NewAppointment = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Edit = new javax.swing.JButton();
        Cancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        TextArea = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("                Automated Receptionist");

        Connect.setText("Connect to Database");
        Connect.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                ConnectAncestorAdded(evt);
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                ConnectAncestorRemoved(evt);
            }
        });
        Connect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectActionPerformed(evt);
            }
        });

        NewAppointment.setBackground(new java.awt.Color(144, 238, 144));
        NewAppointment.setText("New");
        NewAppointment.setActionCommand("Request \nNew Appointment");
        NewAppointment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewAppointmentActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel2.setText("                         Appointments");

        Edit.setBackground(new java.awt.Color(255, 213, 128));
        Edit.setText("Edit");
        Edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditActionPerformed(evt);
            }
        });

        Cancel.setBackground(new java.awt.Color(255, 127, 127));
        Cancel.setText(" Cancel ");
        Cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelActionPerformed(evt);
            }
        });

        TextArea.setEditable(false);
        TextArea.setColumns(20);
        TextArea.setRows(2);
        jScrollPane1.setViewportView(TextArea);

        jLabel3.setText("======================================================");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(84, 84, 84))
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Connect)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(8, 8, 8)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(NewAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(58, 58, 58)
                                        .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(Cancel)))))
                        .addGap(27, 27, 27))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(Connect)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Cancel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Edit, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(NewAppointment, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void ConnectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectActionPerformed
        TikCounter = TikCounter + 1;
        int Condition = TikCounter % 2;
        if (Condition == 1) {
            if (DatabaseConnection() && TableCreation()) {
                TextArea.setText("Database Connection Success");
            } else {
                TextArea.setText("Database Connection Failed");
            }
        } else {
            if (DatabaseDisconnection()) {
                TextArea.setText("Database Successfully Disconnected");
            } else {
                TextArea.setText("Database couldn't Disconnect Successfully");
            }
        }
    }//GEN-LAST:event_ConnectActionPerformed

    private void NewAppointmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewAppointmentActionPerformed
        String MsgID = "Input Your AMKA";
        String ID = JOptionPane.showInputDialog(null, MsgID, MsgID, JOptionPane.QUESTION_MESSAGE);

        String MsgName = "Input Your Name";
        String PatientName = JOptionPane.showInputDialog(null, MsgName, MsgName, JOptionPane.QUESTION_MESSAGE);

        String MsgPhone = "Input Your Phone";
        String PatientPhone = JOptionPane.showInputDialog(null, MsgPhone, MsgPhone, JOptionPane.QUESTION_MESSAGE);

        String MsgHistory = "Input Your Short medical History";
        String PatientHistory = JOptionPane.showInputDialog(null, MsgHistory, MsgHistory, JOptionPane.QUESTION_MESSAGE);

        String MsgDate = "Input Your Requested Date (format: yyyy-mm-dd)";
        String AppointmentDate = JOptionPane.showInputDialog(null, MsgDate, MsgDate, JOptionPane.QUESTION_MESSAGE);

        String MsgDoc = "Input Doctor Type";
        String DoctorType = JOptionPane.showInputDialog(null, MsgDoc, MsgDoc, JOptionPane.QUESTION_MESSAGE);

        Insert(ID, PatientName, AppointmentDate, DoctorType, PatientPhone, PatientHistory);
    }//GEN-LAST:event_NewAppointmentActionPerformed

    private void EditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditActionPerformed
        String MsgID = "Input Your AMKA";
        String ID = JOptionPane.showInputDialog(null, MsgID, MsgID, JOptionPane.QUESTION_MESSAGE);

        String MsgDate = "Input Your Requested Date to be Updated (format: yyyy-mm-dd)";
        String AppointmentDate = JOptionPane.showInputDialog(null, MsgDate, MsgDate, JOptionPane.QUESTION_MESSAGE);

        if (!(ID.isEmpty() && AppointmentDate.isEmpty())) {
            Update(AppointmentDate, ID);
        } else {
            String Message = "No input given...";
            JOptionPane.showMessageDialog(null, Message, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_EditActionPerformed

    private void CancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelActionPerformed
        String MsgID = "Input Your AMKA for deletion";
        String ID = JOptionPane.showInputDialog(null, MsgID, MsgID, JOptionPane.QUESTION_MESSAGE);

        String ConMsg = "Are you sure you want to Cancel your appointment?";
        String Affirm = "Confirm Action for AMKA " + ID;
        int Confirm = JOptionPane.showConfirmDialog(null, ConMsg, Affirm, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (!(ID.isEmpty()) && Confirm == JOptionPane.YES_OPTION) {
            Detele(ID);
        } else {
            TextArea.setText("Deletion Not Proceeded");
        }
    }//GEN-LAST:event_CancelActionPerformed

    private void ConnectAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_ConnectAncestorAdded
        if (DatabaseConnection() && TableCreation()) {
            TextArea.setText("Database Connection Success");
        } else {
            TextArea.setText("Database Connection Failed");
        }
    }//GEN-LAST:event_ConnectAncestorAdded

    private void ConnectAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_ConnectAncestorRemoved
        if (DatabaseDisconnection()) {
            TextArea.setText("Database Successfully Disconnected");
        } else {
            TextArea.setText("Database couldn't Disconnect Successfully");
        }
    }//GEN-LAST:event_ConnectAncestorRemoved

    public static void main(String args[]) {
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SofTechGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SofTechGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SofTechGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SofTechGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SofTechGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Cancel;
    private javax.swing.JCheckBox Connect;
    private javax.swing.JButton Edit;
    private javax.swing.JButton NewAppointment;
    private javax.swing.JTextArea TextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
