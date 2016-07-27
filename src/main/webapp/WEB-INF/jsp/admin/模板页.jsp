<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/admin/common/_tags.jsp" %>
<c:set var="_page_tab" value="XXXXXX"></c:set>
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
                Page Header
                <small>Optional description</small>
            </h1>
            <ol class="breadcrumb">
                <li><a href="${path}/admin/index"><i class="fa fa-dashboard"></i> 首页</a></li>
                <li class="active">Here</li>
            </ol>
        </section>

        <!-- Main content -->
        <section class="content">

            <!-- Your Page Content Here -->
            <div class="box box-info">
            </div>

        </section>
        <!-- /.content -->

    </div>
    <%@ include file="/WEB-INF/jsp/admin/common/footer.jsp" %>
    <%@ include file="/WEB-INF/jsp/admin/common/sidebar.jsp" %>
</div>
<%@ include file="/WEB-INF/jsp/admin/common/_include2.jsp" %>
</body>
</html>
