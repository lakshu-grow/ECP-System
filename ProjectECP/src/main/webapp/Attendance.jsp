
<%@ page import="java.sql.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Attendance Management</title>
  <link rel="stylesheet" href="attendance.css">
</head>
<body>
  <div class="attendance-container">
  <div class="container">
    <h1>Attendance Management</h1>
    <form action="AttendanceServlet" method="post">
      <table>
        <thead>
          <tr>
            <th>Register No</th>
            <th>Cadet Name</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <%
            try {
              Class.forName("com.mysql.cj.jdbc.Driver");
              Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/corps","root","");
              Statement st = con.createStatement();
              ResultSet rs = st.executeQuery("SELECT reg_no, cadet_name FROM cadet_details ORDER BY cadet_name ASC");

              while(rs.next()) {
                String reg = rs.getString("reg_no");
                String name = rs.getString("cadet_name");
          %>
          <tr>
            <td><%= reg %></td>
            <td><%= name %></td>
            <td>
              <button type="button" class="present-btn" 
                onclick="toggleStatus(this, document.getElementById('status_<%= reg %>'))">
                Present
              </button>
              <input type="hidden" name="status_<%= reg %>" id="status_<%= reg %>" value="Present">
            </td>
          </tr>
          <% 
              }
              con.close();
            } catch(Exception e) { out.print(e); }
          %>
        </tbody>
      </table>
      <button type="submit" class="save-btn">Save Attendance</button>
    </form>
  </div>
</div>
  <script>
    function toggleStatus(btn, hiddenInput) {
      if (btn.innerText === "Present") {
        btn.innerText = "Absent";
        btn.classList.remove("present-btn");
        btn.classList.add("absent-btn");
        hiddenInput.value = "Absent";
      } else {
        btn.innerText = "Present";
        btn.classList.remove("absent-btn");
        btn.classList.add("present-btn");
        hiddenInput.value = "Present";
      }
    }
  </script>
</body>
</html>
