<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>showError.jsp</title>
</head>
<body>

	<%=request.getAttribute("errorMsg")%>

	<br>

	<a href="login.html"> login 화면으로 이동</a>
	<!-- html 파일 링크는 마음껏 변경하셔도 됩니다. -->


</body>
</html>