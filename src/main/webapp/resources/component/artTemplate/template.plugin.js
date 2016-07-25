//simpleIf(var,c1,v1,c2,v2,...,cn,vn)
//var == c1 返回 v1
//var == c2 返回 v2
//.......
//var == cn 返回 vn
//否则返回空字符串
template.helper('simpleIf', function () {
    var length = arguments.length;
    if (length < 2 || length % 2 == 0) {
        return '';
    }
    for (var i = 1; i < length; i += 2) {
        if (arguments[0] == arguments[i]) {
            return arguments[i + 1];
        }
    }
    return '';
});

template.helper('valueIfDefault', function (value, defaultValue) {
    if (value) {
        return value;
    }
    return defaultValue;
});

template.helper('dateFormat', function (time, fmt) {
    if (!time) {
        return '';
    }
    //修正；"2015-12-31 00:00:00" 转化为 "2015/12/31 00:00:00";
    //转化为各个浏览器最兼容的一种方式（yyyy/MM/dd）；
    var date = new Date(time);
    var o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
        "q+": Math.floor((date.getMonth() + 3) / 3),
        "S": date.getMilliseconds()
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
});

template.helper('isNotBlank', function (value) {
    return value;
});
template.helper('subString', function (str, startIndex, length, appendStr) {
    if (!str) {
        return "";
    }
    if (str.length < length) {
        return str;
    }
    return str.substring(startIndex, length) + (appendStr == null ? '' : appendStr);
});
template.helper('parseInt', function (number) {
    return parseInt(number);
});
template.helper('toFixed', function (number,digit) {
    return number.toFixed(digit);
});
template.helper('containerStr', function (value, str) {
    if (!value) {
        return false;
    }
    if (!str) {
        return false;
    }
    return value.toString().indexOf(str) != -1;
});
template.helper('generPagination', function (pageNo, totalPage, totalRecord) {

    //生成分页控件
    kkpager.generPageHtml({
        pagerid: 'kkpager',
        pno: pageNo,
        mode: 'click', //设置为click模式
        //总页码
        total: totalPage,
        //总数据条数
        totalRecords: totalRecord,
        isGoPage: false,
        isShowCurrPage: false,
        isShowTotalPage: false,
        //点击页码、页码输入框跳转、以及首页、下一页等按钮都会调用click
        //适用于不刷新页面，比如ajax
        click: function (n) {
            //这里可以做自已的处理
            paginationCallback(n);
            //处理完后可以手动条用selectPage进行页码选中切换
            this.selectPage(n);
            return false;
        },
        //getHref是在click模式下链接算法，一般不需要配置，默认代码如下
        getHref: function (n) {
            return '#';
        }

    }, true);
});
template.helper('divide', function (arg1,arg2) {
    if (arg1 == null) {
        arg1 = 0;
    }
    if (arg2 == null) {
        arg2 = 0;
    }
    if (arg1 == 0 || arg2 == 0) {
        return 0;
    }
    var t1=0,t2=0,r1,r2;
    try{t1=arg1.toString().split(".")[1].length}catch(e){}
    try{t2=arg2.toString().split(".")[1].length}catch(e){}
    with(Math){
        r1=Number(arg1.toString().replace(".",""))
        r2=Number(arg2.toString().replace(".",""))
        return (r1/r2)*pow(10,t2-t1);
    }
});
template.helper('dueDay', function (dateStr) {
    if(!dateStr){
        return 0;
    }
    var day = Math.ceil((new Date(dateStr.replace("T", " ").replace(/-/g, "/")).getTime() - new Date().getTime()) / (24 * 3600 * 1000));
    return day > 0 ? day : 0;
});