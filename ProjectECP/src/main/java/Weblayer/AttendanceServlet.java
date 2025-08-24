package Weblayer;

import java.io.*;
import java.sql.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", "");

            // Step 1: fetch cadets
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT reg_no FROM cadet_details");

            // Step 2: insert attendance
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO attendance(reg_no, date, status) VALUES(?, CURDATE(), ?)"
            );

            while (rs.next()) {
                String regNo = rs.getString("reg_no");
                String status = request.getParameter("status_" + regNo);

                if (status != null) {
                    ps.setString(1, regNo);
                    ps.setString(2, status);
                    ps.executeUpdate();

                    // Step 3: calculate percentage
                    PreparedStatement ps1 = con.prepareStatement(
                        "SELECT COUNT(*) FROM attendance WHERE reg_no=?");
                    ps1.setString(1, regNo);
                    ResultSet totalRS = ps1.executeQuery();
                    totalRS.next();
                    int total = totalRS.getInt(1);

                    PreparedStatement ps2 = con.prepareStatement(
                        "SELECT COUNT(*) FROM attendance WHERE reg_no=? AND status='Present'");
                    ps2.setString(1, regNo);
                    ResultSet presentRS = ps2.executeQuery();
                    presentRS.next();
                    int present = presentRS.getInt(1);

                    double percentage = (present * 100.0) / total;

                    // Step 4: update cadet_details
                    PreparedStatement ps3 = con.prepareStatement(
                        "UPDATE cadet_details SET attendance_percent=? WHERE reg_no=?");
                    ps3.setDouble(1, percentage);
                    ps3.setString(2, regNo);
                    ps3.executeUpdate();
                }
            }

            HttpSession session = request.getSession();
            session.setAttribute("message", "Attendance saved & performance updated!");

            // Redirect back to Admin.html
            response.sendRedirect("Admin.html");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("Error while saving attendance: " + e.getMessage());
        }
    }
}
