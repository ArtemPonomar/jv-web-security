package mate.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import mate.exception.AuthenticationException;
import mate.lib.Injector;
import mate.model.Driver;
import mate.service.AuthService;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    private static final Injector injector = Injector.getInstance("mate");
    private final AuthService authService = (AuthService) injector.getInstance(AuthService.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            final Driver driver = authService.login(
                    req.getParameter("login"),
                    req.getParameter("password"));
            final HttpSession session = req.getSession();
            session.setAttribute("driverId", driver.getId());
            resp.sendRedirect("/");
        } catch (AuthenticationException e) {
            req.setAttribute("errorMessage", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }
}