package controller;

import java.io.IOException;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.SignUpDAO;
import model.dto.MemberDTO;

@WebServlet("/controller")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		response.setContentType("text/html;charset=utf-8");

		String command = request.getParameter("command");

		// PrintWriter out = response.getWriter();

		try {
			if (command != null && command.length() != 0) {

				if (command.equals("sign_up")) { // 회원가입
					signUp(request, response);
				} else if (command.equals("passwordUpdate")) {// name으로 찾은 데이터의 pw 변경
					passwordUpdate(request, response);
				} else if (command.equals("findAll")) {
					getAllMembers(request, response);
				} else if (command.equals("findOne")) {
					getMemberById(request, response);
				} else if (command.equals("findId")) {
					getIdByName(request, response);
				} else if (command.equals("removeMember")) {
					removeMemberById(request, response);
				} else if (command.equals("login")) {
					loginMember(request, response);
				} else if (command.equals("checkId")) {
					checkId(request, response);
				} else {
					request.setAttribute("errorMsg", "해당 command 미존재");
					request.getRequestDispatcher("showError.jsp").forward(request, response);
				}
			}
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	protected void signUp(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");
		String re_pw = request.getParameter("re_pw");
		String name = request.getParameter("name");

		try {
			if (pw.equals(re_pw)) {
				boolean add = SignUpDAO.addMember(id, pw, name);

				if (add) {
					request.setAttribute("successMsg", name);
					request.getRequestDispatcher("successSignUp.jsp").forward(request, response);
				}
			} else {
				request.setAttribute("errorMsg", "비밀번호 불일치");
				request.getRequestDispatcher("showErrorSignUp.jsp").forward(request, response);
			}
		} catch (RollbackException s) {
			request.setAttribute("errorMsg", "이미 존재하는 id 입니다.");
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}

	}

	protected void passwordUpdate(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			boolean result = SignUpDAO.updateInfo(request.getParameter("id"), request.getParameter("pw"));
			if (result) {
				request.setAttribute("member", SignUpDAO.getMember(request.getParameter("id")));
				request.getRequestDispatcher("success.jsp").forward(request, response);
			} else {
				request.setAttribute("errorMsg", "수정 실패");
				request.getRequestDispatcher("showError.jsp").forward(request, response);
			}
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	protected void getAllMembers(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			List<MemberDTO> list = SignUpDAO.getAllMembers();

			request.setAttribute("findAllMembers", list);
			request.getRequestDispatcher("success.jsp").forward(request, response);
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}

	}

	protected void getMemberById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = request.getParameter("name");

		try {
			MemberDTO memberDTO = SignUpDAO.getPw(id, name);

			request.setAttribute("findmemberById", memberDTO);
			request.getRequestDispatcher("find.jsp").forward(request, response);
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	protected void getIdByName(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String name = request.getParameter("name");

		try {
			MemberDTO memberDTO = SignUpDAO.getMember(name);

			request.setAttribute("findIdByName", memberDTO);
			request.getRequestDispatcher("findnamesuccess.jsp").forward(request, response);
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	protected void removeMemberById(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		try {

			boolean removeMember = SignUpDAO.removeMember(id);

			if (removeMember) {
				request.setAttribute("successMsg", "삭제 완료");
				request.getRequestDispatcher("success.jsp").forward(request, response);
			} else {
				request.setAttribute("errorMsg", "삭제 실패. 존재하는 데이터인지 확인하세요.");
				request.getRequestDispatcher("showError.jsp").forward(request, response);
			}
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showError.jsp").forward(request, response);
			s.printStackTrace();
		}
	}

	protected void loginMember(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String pw = request.getParameter("pw");

		try {
			MemberDTO member = SignUpDAO.loginMember(id, pw);

			request.setAttribute("successMsg", member.getName());
			request.getRequestDispatcher("successLogin.jsp").forward(request, response);

		} catch (NoResultException s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showErrorLogin.jsp").forward(request, response);
			s.printStackTrace();
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showErrorLogin.jsp").forward(request, response);
			s.printStackTrace();
		}

	}

	protected void checkId(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("id");

		try {
			boolean chkId = SignUpDAO.checkIdDuplicate(id);

			if (chkId) {
				request.setAttribute("errorMsg", "저장 가능한 id 입니다.");
				request.getRequestDispatcher("showErrorSignUp.jsp").forward(request, response);
			} else {
				request.setAttribute("errorMsg", "이미 있는 id 입니다.");
				request.getRequestDispatcher("showErrorSignUp.jsp").forward(request, response);
			}
		} catch (Exception s) {
			request.setAttribute("errorMsg", s.getMessage());
			request.getRequestDispatcher("showErrorLogin.jsp").forward(request, response);
			s.printStackTrace();
		}
	}
}