package Weblayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/Validation")
public class Validation extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	//doPost(request,response);

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

       String user = request.getParameter("uname");
        String pass = request.getParameter("pwd");

        try {
            if (user.equals("staff") && pass.equals("staff123")) {
                out.println("Welcome admin");
                RequestDispatcher rd=request.getRequestDispatcher("Admin.html");
            	rd.forward(request, response);
            } else {
                // 1. Load MySQL JDBC Driver (correct class name)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // 2. Connect to database (use correct username and password)
                Connection con = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/corps", "root", "");

                // 3. Check for user match
                Statement stmt = con.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT * FROM register");

                boolean found = false;
                while (rs.next()) {
                    String dbUser = rs.getString("username");
                    String dbPass = rs.getString("password");

                    if (user.equals(dbUser) && pass.equals(dbPass)) {
                        out.println("Welcome user");
                        found = true;
                    	RequestDispatcher rd=request.getRequestDispatcher("UserHome.html");
                    	rd.forward(request, response);
                        break;
                    }
                }

                if (!found) {
                    response.sendRedirect("index.html?error=1");
                }


                con.close();
            }
        	

        } catch (Exception e) {
            out.println("Error: " + e.getMessage());
        }
    }
}