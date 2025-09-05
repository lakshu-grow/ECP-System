<%@ page import="java.sql.*, java.util.*" %>
<%
    request.setCharacterEncoding("UTF-8");
    String method = request.getMethod();

    String username = request.getParameter("username");
    String password = request.getParameter("password");

    String reg_no = "";
    String cadetName = "";
    int percentage = -1; // -1 means login not done
    int absentPercent = 0;
    List<String> absentDates = new ArrayList<>();
    String error = "";

    if ("POST".equalsIgnoreCase(method)) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/corps","root","");

            // 1️⃣ Validate username & password in register table
            ps = con.prepareStatement("SELECT * FROM register WHERE username=? AND password=?");
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                // 2️⃣ Get cadet details (join by cadet_name = username)
                ps = con.prepareStatement("SELECT reg_no, cadet_name, attendance_percent FROM cadet_details WHERE cadet_name=?");
                ps.setString(1, username);   // use username directly to match cadet_name
                rs = ps.executeQuery();

                if (rs.next()) {
                    reg_no = rs.getString("reg_no");
                    cadetName = rs.getString("cadet_name");
                    percentage = rs.getInt("attendance_percent");
                    absentPercent = 100 - percentage;
                }

                // 3️⃣ Get absent dates
                ps = con.prepareStatement("SELECT date FROM attendance WHERE reg_no=? AND status='Absent' ORDER BY date ASC");
                ps.setString(1, reg_no);
                rs = ps.executeQuery();
                while (rs.next()) {
                    absentDates.add(rs.getString("date"));
                }
            } else {
                error = "Invalid Username or Password!";
            }

        } catch (Exception e) {
            error = "Error: " + e.getMessage();
        }
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>My Attendance</title>
    <link rel="stylesheet" href="attendance.css">
</head>
<body>
<div class="view-attendance-container">

    <div class="user-header">
        <h1>My Attendance</h1>
        <div>
          <a href="Cadet.html">← Back</a> |
          <a href="Login.html">Logout</a>
        </div>
    </div>

    <%
      // If not logged in → show login form
      if (percentage == -1) {
    %>
        <form method="post" class="login-form">
            <h3>Enter Credentials</h3>
            <% if(!error.isEmpty()) { %>
              <p style="color:red;"><%=error%></p>
            <% } %>
            <label>Username:</label><br>
            <input type="text" name="username" required><br><br>
            <label>Password:</label><br>
            <input type="password" name="password" required><br><br>
            <button type="submit">View Attendance</button>
        </form>
    <%
      } else {
    %>
        <!-- Circle showing attendance percentage -->
        <div class="circle" style="--present:<%=percentage%>; --absent:<%=absentPercent%>;">
          <div class="circle-text">
            <%=percentage%>% Present
            <br>
            <span><%=absentPercent%>% Absent</span>
          </div>
        </div>
        <h2 style="text-align:center;"><%=cadetName%></h2>

        <h3 class="absent-title">Absent Dates</h3>
        <table>
            <tr><th>Date</th></tr>
            <% for(String d : absentDates){ %>
                <tr><td><%=d%></td></tr>
            <% } if(absentDates.size()==0){ %>
                <tr><td>No Absents 🎉</td></tr>
            <% } %>
        </table>
    <%
      }
    %>

</div>
</body>
</html>
