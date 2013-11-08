package net.modd.chemistry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ChemServlet extends HttpServlet{
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		ChemFiles.loadFiles();
		String data = req.getParameter("command");
		PrintWriter print = resp.getWriter();
		ChemShell.processLine(new Scanner(data),print);
	}
}