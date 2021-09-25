package net.ev.web;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.ev.dao.EvDao;
import net.ev.dao.EvDaoImpl;
import net.ev.model.EV;





@WebServlet("/")
public class EvController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private EvDao evDAO;

	public void init() {
		evDAO = new EvDaoImpl();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();

		try {
			switch (action) {
			case "/new":
				showNewForm(request, response);
				break;
			case "/insert":
				insertEv(request, response);
				break;
			case "/delete":
				deleteEv(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/update":
				updateEv(request, response);
				break;
			case "/list":
				listEv(request, response);
				break;
			default:
				RequestDispatcher dispatcher = request.getRequestDispatcher("login/login.jsp");
				dispatcher.forward(request, response);
				break;
			}
		} catch (SQLException ex) {
			throw new ServletException(ex);
		}
	}

	private void listEv(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException, ServletException {
		List<EV> listEv = evDAO.selectAllEvs();
		request.setAttribute("listEv", listEv);
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo-list.jsp");
		dispatcher.forward(request, response);
	}

	private void showNewForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo-form.jsp");
		dispatcher.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, ServletException, IOException {
		int vehicle_no = Integer.parseInt(request.getParameter("vehicle_no"));
		EV existingEv = evDAO.selectEv(vehicle_no);
		RequestDispatcher dispatcher = request.getRequestDispatcher("todo-form.jsp");
		request.setAttribute("todo", existingEv);
		dispatcher.forward(request, response);

	}

	private void insertEv(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		
		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");
		
		/*DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		LocalDate targetDate = LocalDate.parse(request.getParameter("targetDate"),df);*/
		
		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		EV newEv = new EV(title, username, description, LocalDate.now(), isDone);
		evDAO.insertEv(newEv);
		response.sendRedirect("list");
	}

	private void updateEv(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int vehicle_no = Integer.parseInt(request.getParameter("vehicle_no"));
		
		String title = request.getParameter("title");
		String username = request.getParameter("username");
		String description = request.getParameter("description");
		//DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		LocalDate serviceDate = LocalDate.parse(request.getParameter("serviveDate"));
		
		boolean isDone = Boolean.valueOf(request.getParameter("isDone"));
		EV updateEv = new EV(vehicle_no, title, username, description, serviceDate, isDone);
		
		evDAO.updateEv(updateEv);
		
		response.sendRedirect("list");
	}

	private void deleteEv(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
		int vehicle_no = Integer.parseInt(request.getParameter("vehicle_no"));
		evDAO.deleteEv(vehicle_no);
		response.sendRedirect("list");
	}
}
