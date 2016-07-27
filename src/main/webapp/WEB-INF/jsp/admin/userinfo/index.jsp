<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/admin/common/_tags.jsp" %>
<c:set var="_page_tab" value="userinfo"></c:set>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/jsp/admin/common/_include1.jsp" %>
    <%@ include file="/WEB-INF/jsp/admin/common/_param.jsp" %>
    <%@ include file="/WEB-INF/jsp/admin/common/_job.jsp" %>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
    <%@ include file="/WEB-INF/jsp/admin/common/header.jsp" %>
    <%@ include file="/WEB-INF/jsp/admin/common/left.jsp" %>

    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
        <!-- Content Header (Page header) -->
        <section class="content-header">
            <h1>
                个人信息
                <small>当前用户信息</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${path}/admin/index"><i class="fa fa-dashboard"></i> 首页</a></li>
                <li class="active">个人信息</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <div class="box box-info">
                <div class="form-horizontal">
                    <div class="box-body">

                        <div class="form-group">
                            <label class="col-sm-2 control-label"></label>

                            <div class="col-sm-5">
                                <img src="${path}/resources/img/gly.jpg" class="img-rounded"
                                     style="width: 123px;height: 123px;">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">用户名</label>

                            <div class="col-sm-5">
                                ${adminUser.userName}
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">名称</label>

                            <div class="col-sm-5">
                                ${adminUser.showName}
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-2 control-label">创建时间</label>

                            <div class="col-sm-5">
                                <c:if test="${not empty adminUser.createTime}">
                                    <fmt:formatDate value="${adminUser.createTime}" pattern="yyyy-MM-dd HH:mm"></fmt:formatDate>
                                </c:if>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <!-- Your Page Content Here -->
        </section>
        <!-- /.content -->

    </div>
    <%@ include file="/WEB-INF/jsp/admin/common/footer.jsp" %>
    <%@ include file="/WEB-INF/jsp/admin/common/sidebar.jsp" %>
</div>
<%@ include file="/WEB-INF/jsp/admin/common/_include2.jsp" %>
</body>
</html>
