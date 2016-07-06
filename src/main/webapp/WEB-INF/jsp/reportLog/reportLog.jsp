<%@ page import="com.robotsafebox.enums.ReportLogActionEnum" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!--引入jstl -->
<%@include file="../common/tag.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>硬件上传日志</title>
    <%@include file="../common/head.jsp" %>
</head>
<body>
<!--页面显示部分 -->
<div class="container">
    <div class="panel panel-default">
        <div class="panel-heading text-center">
            <h2>硬件上传日志(最新100条)</h2>
        </div>
        <div class="panel-body">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>ICHID</th>
                    <th>上报类型</th>
                    <th>创建时间</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="sk" items="${list}">
                    <tr>
                        <td>${sk.id}</td>
                        <td>${sk.ichId}</td>
                            <%--<td>${sk.actionType}</td>--%>
                        <td>
                            <c:forEach var="atype" items="<%=ReportLogActionEnum.values()%>">
                                <c:if test="${atype.action==sk.actionType}">
                                    ${atype.info}
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <fmt:formatDate value="${sk.createTime}" pattern="yyyy-MM-dd HH:mm:ss"></fmt:formatDate>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="http://apps.bdimg.com/libs/jquery/2.0.0/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="http://apps.bdimg.com/libs/bootstrap/3.3.0/js/bootstrap.min.js"></script>
</html>