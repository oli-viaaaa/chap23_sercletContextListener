package com.javalab.listener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import com.javalab.dao.BoardDao;

/*
 * 톰캣이 구동 될 때 불려지는 클래스로 필터 보다 먼저 불려진다. 
 */
@WebListener
public class ContextListener implements ServletContextListener{
	
	public DataSource dataSource = null;
	BoardDao boardDao = null;
	
	/*
	 *  톰캣이 구동 될 때 불려지는 메소드
	 *    - ServletContextEvent event : event에 서블릿 컨텍스트 정보가 있다.
	 */
	
	public void contextInitialized(ServletContextEvent event) {
		// [디버깅]
		System.out.println("ContextListener의 contextInitialized 메소드");
		
		try {
			// 톰캣이 넣어주는 event 객체로부터 서블릿 컨텍스트 정보 추출
			ServletContext servletContext = event.getServletContext();
			
			// JNDI 서비스를 통해서 서버자원 찾아 사용하기
			Context ctx = new InitialContext();
			Context envContext = (Context) ctx.lookup("java:/comp/env");
			dataSource = (DataSource) envContext.lookup("jdbc/oracle");
			
			boardDao = new BoardDao(); // BoardDao 객체 생성
			boardDao.setDataSource(dataSource); // BoardDao 객체에 DataSource
			
			// BoardDao 객체를 서블릿 컨텍스트에 저장
			// 이제 서블릿 어디서나 BoardDao 객체를 꺼내서 쓸 수 있게됨.
			servletContext.setAttribute("boardDao", boardDao);
			// [디버깅]
			System.out.println("BoardDao 객체 서블릿 컨텍스트에 저장 완료!");
			
					} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
