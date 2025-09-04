package Weblayer;

import java.io.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/UploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String comp = request.getParameter("comp"); // drill, swimming, etc.
    Part filePart = request.getPart("file");

    // Save path inside competitions folder
    String fileName = filePart.getSubmittedFileName();
    String savePath = getServletContext().getRealPath("/competitions/" + comp + "/" + fileName);

    File file = new File(savePath);
    filePart.write(file.getAbsolutePath());

    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    out.println("<h3>Source Updated Successfully!</h3>");
    out.println("<a href='competition_view.html?comp=" + comp + "'>Back to View</a>");
  }
}
