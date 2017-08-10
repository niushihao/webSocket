package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by nsh on 2017/8/10 0010.
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet{

    private static int num=0;
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String name = request.getParameter("name");
        System.out.println("name="+name);
        //将当前用户的信息存入session中
        request.getSession().setAttribute("name",name);
        //重定向到聊天界面
        response.sendRedirect("index.html");
    }
}
