<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!-- Left side column. contains the logo and sidebar -->
<aside class="main-sidebar">

  <!-- sidebar: style can be found in sidebar.less -->
  <section class="sidebar">

    <!-- Sidebar user panel (optional) -->
    <div class="user-panel">
      <div class="pull-left image">
        <img src="${path}/resources/img/gly.jpg" class="img-circle" alt="${not empty adminUser.showName ? adminUser.showName  : adminUser.userName}">
      </div>
      <div class="pull-left info">
        <p>${not empty adminUser.showName ? adminUser.showName  : adminUser.userName}</p>
        <!-- Status -->
        <a href="#"><i class="fa fa-circle text-success"></i> Online</a>
      </div>
    </div>

    <%--<!-- search form (Optional) -->--%>
    <%--<form action="#" method="get" class="sidebar-form">--%>
      <%--<div class="input-group">--%>
        <%--<input type="text" name="q" class="form-control" placeholder="Search...">--%>
              <%--<span class="input-group-btn">--%>
                <%--<button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>--%>
                <%--</button>--%>
              <%--</span>--%>
      <%--</div>--%>
    <%--</form>--%>
    <%--<!-- /.search form -->--%>

    <!-- Sidebar Menu -->
    <ul class="sidebar-menu">
      <li class="header">导航菜单</li>
      <!-- Optionally, you can add icons to the links -->
      <li class="active"><a href="#"><i class="fa fa-dashboard"></i> <span>首页</span></a></li>
      <li><a href="#"><i class="fa fa-user"></i> <span>个人信息</span></a></li>
      <li class="treeview">
        <a href="#"><i class="fa fa-link"></i> <span>其他</span>
            <span class="pull-right-container">
              <i class="fa fa-angle-left pull-right"></i>
            </span>
        </a>
        <ul class="treeview-menu">
          <li><a href="#">其他1</a></li>
          <li><a href="#">其他2</a></li>
        </ul>
      </li>
    </ul>
    <!-- /.sidebar-menu -->
  </section>
  <!-- /.sidebar -->
</aside>