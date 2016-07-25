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
                        <form role="form" action="" method="post" class="login-form">
                            <div class="form-group">
                                <label class="sr-only" for="form-username">Username</label>
                                <input type="text" name="form-username" placeholder="用户名"
                                       class="form-username form-control" id="form-username">
                            </div>
                            <div class="form-group">
                                <label class="sr-only" for="form-password">Password</label>
                                <input type="password" name="form-password" placeholder="密码"
                                       class="form-password form-control" id="form-password">
                            </div>
                            <button type="submit" class="btn">登录</button>
                        </form>
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
<script>
    jQuery(document).ready(function () {

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

        $('.login-form').on('submit', function (e) {
            $(this).find('input[type="text"], input[type="password"], textarea').each(function () {
                if ($(this).val() == "") {
                    e.preventDefault();
                    $(this).addClass('input-error');
                }
                else {
                    $(this).removeClass('input-error');
                }
            });
        });

    });
</script>


<!--[if lt IE 10]>
<script src="${path}/resources/component/adminLogin/assets/js/placeholder.js"></script>
<![endif]-->

</body>

</html>