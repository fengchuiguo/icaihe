<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%--<html lang="en">--%>
<html>

<head>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>${_adminWebTitle}-登录</title>

    <!-- CSS -->
    <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:400,100,300,500">
    <link rel="stylesheet" href="${path}/resources/component/adminLogin/assets/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="${path}/resources/component/adminLogin/assets/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${path}/resources/component/adminLogin/assets/css/form-elements.css">
    <link rel="stylesheet" href="${path}/resources/component/adminLogin/assets/css/style.css">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

    <!-- Favicon and touch icons -->
    <link rel="shortcut icon" href="${path}/resources/component/adminLogin/assets/ico/favicon.png">
    <link rel="apple-touch-icon-precomposed" sizes="144x144"
          href="${path}/resources/component/adminLogin/assets/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114"
          href="${path}/resources/component/adminLogin/assets/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72"
          href="${path}/resources/component/adminLogin/assets/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed"
          href="${path}/resources/component/adminLogin/assets/ico/apple-touch-icon-57-precomposed.png">

</head>

<body>

<!-- Top content -->
<div class="top-content">

    <div class="inner-bg">
        <div class="container">
            <div class="row">
                <div class="col-sm-8 col-sm-offset-2 text">
                    <h1><strong>${_logoName}</strong></h1>
                    <%--<div class="description">--%>
                    <%--<p>${_adminWebTitle}</p>--%>
                    <%--</div>--%>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6 col-sm-offset-3 form-box">
                    <div class="form-top">
                        <div class="form-top-left">
                            <h3>登录</h3>

                            <p>${_adminWebTitle}</p>
                        </div>
                        <div class="form-top-right">
                            <i class="fa fa-key"></i>
                        </div>
                    </div>
                    <div class="form-bottom">
                        <form method="post" class="login-form">
                            <div class="form-group">
                                <label class="sr-only" for="username">Username</label>
                                <input type="text" name="username" placeholder="用户名" maxlength="20"
                                       class="form-username form-control" id="username">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="password">Password</label>
                                <input type="password" name="password" placeholder="密码" maxlength="20"
                                       class="form-password form-control" id="password">
                            </div>
                        </form>

                        <button id="loginbutton" class="btn" style="width: 100%;">登录</button>

                    </div>
                </div>
            </div>
        </div>
    </div>

</div>


<!-- Javascript -->
<script src="${path}/resources/component/adminLogin/assets/js/jquery-1.11.1.min.js"></script>
<script src="${path}/resources/component/adminLogin/assets/bootstrap/js/bootstrap.min.js"></script>
<script src="${path}/resources/component/adminLogin/assets/js/jquery.backstretch.min.js"></script>

<script src="${path}/resources/component/layer/web/layer.js"></script>

<%@ include file="/WEB-INF/jsp/admin/common/_param.jsp" %>

<script>
    jQuery(document).ready(function () {

        <c:if test="${not empty adminUser}">
        window.location.href = "${path}/admin/index";
        </c:if>

        /*
         Fullscreen background
         */
        $.backstretch("${path}/resources/component/adminLogin/assets/img/backgrounds/1.jpg");

        /*
         Form validation
         */
        $('.login-form input[type="text"], .login-form input[type="password"], .login-form textarea').on('focus', function () {
            $(this).removeClass('input-error');
        });

        $('#loginbutton').bind("click", function (e) {

            var emptyFlag = 0;

            $('.login-form').find('input[type="text"], input[type="password"], textarea').each(function () {
                if ($(this).val() == "") {
                    e.preventDefault();
                    $(this).addClass('input-error');
                    emptyFlag = 1;
                }
                else {
                    $(this).removeClass('input-error');
                }
            });

            if (emptyFlag == 0) {
                $.ajax({
                    url: "${path}/doAdminLogin",
                    async: false,
                    cache: false,
                    type: 'post',
                    dataType: "json",
                    data: $('.login-form').serialize(),
                    success: function (data) {
                        if (data.state == '1') {
                            window.location.href = "${path}/admin/index";
                        } else {
                            layer.msg(data.message, {time: _layer_msg_time});
                        }
                    },
                    error: function (xhr, type) {
                        layer.msg("登录失败", {time: _layer_msg_time});
                    }
                });
            }

        });

    });
</script>


<!--[if lt IE 10]>
<script src="${path}/resources/component/adminLogin/assets/js/placeholder.js"></script>
<![endif]-->

</body>

</html>