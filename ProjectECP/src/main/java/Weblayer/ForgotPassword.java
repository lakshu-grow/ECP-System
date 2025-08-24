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

@WebServlet("/ForgotPassword")
public class ForgotPassword extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String uname = request.getParameter("uname");
        String newpwd = request.getParameter("newpwd");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load driver & connect
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/corps", "root", ""); // update password if needed

            String query = "UPDATE register SET password = ? WHERE username = ?";
            PreparedStatement pst = con.prepareStatement(query);
            pst.setString(1, newpwd);
            pst.setString(2, uname);
            int updated = pst.executeUpdate();

            if (updated > 0) {
                out.println("<script>alert('Password updated successfully!'); window.location='index.html';</script>");
            } else {
                out.println("<script>alert('Username not found. Try again.'); window.location='ForgotPassword.html';</script>");
            }

            pst.close();
            con.close();

        } catch (Exception e) {
            out.println("<script>alert('Error: " + e.getMessage() + "'); window.location='ForgotPassword.html';</script>");
        }
    }
}
