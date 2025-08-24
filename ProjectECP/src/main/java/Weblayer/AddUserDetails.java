package Weblayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AddUserDetails")
public class AddUserDetails extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String uname = request.getParameter("uname");
        String regno = request.getParameter("regno");
        String email = request.getParameter("email");
        String pwd = request.getParameter("pwd");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load driver & connect
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", ""); // change password if needed

            // Insert into register table
            String query1 = "INSERT INTO register (username, password) VALUES (?, ?)";
            PreparedStatement pst1 = con.prepareStatement(query1);
            pst1.setString(1, uname);
            pst1.setString(2, pwd);
            int rows1 = pst1.executeUpdate();

            // Insert into cadet_details table
            String query2 = "INSERT INTO cadet_details (cadet_name, reg_no, email, attendance_percent, competition_marks) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst2 = con.prepareStatement(query2);
            pst2.setString(1, uname);
            pst2.setString(2, regno);
            pst2.setString(3, email);
            pst2.setDouble(4, 0.0);  // default attendance
            pst2.setInt(5, 0);       // default competition mark
            int rows2 = pst2.executeUpdate();

            if (rows1 > 0 && rows2 > 0) {
                out.println("<script>alert('Registered Successfully!'); window.location='index.html';</script>");
            } else {
                out.println("<script>alert('Failed to register. Try again.'); window.location='Register.html';</script>");
            }

            pst1.close();
            pst2.close();
            con.close();

        } catch (Exception e) {
            out.println("<script>alert('Error: " + e.getMessage() + "'); window.location='Register.html';</script>");
        }
    }
}
