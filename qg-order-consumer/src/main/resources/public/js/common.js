var environment=0; //0表示测试环境 1:线上环境
var dialog={};
var ReqUrl={
    GoodsReqUrl:function () {
        return (environment==0?"http://192.168.101.4:82/api/":"http://goods.local.com/api/");
    },
    OrdeReqUrl:function () {
        return (environment==0?"http://192.168.101.4:83/api/":"http://order.local.com/api/");
    },
    LoginReqUrl:function() {
        return (environment==0?"http://192.168.101.4:8080/api/":"http://user.local.com/api/");
    },
    PayReqUrl:function () {
        return (environment==0?"http://192.168.101.4:84/api/":"http://pay.local.com/api/");
    }
};
//设置cookie
window.setCookie=function(c_name,c_value,expiredays){
    var exdate=new Date()
    exdate.setDate(exdate.getDate()+expiredays)
    document.cookie=c_name+ "=" +escape(c_value)+((expiredays==null) ? "" : ";expires="+exdate.toGMTString());
};
//读取cookie
window.getCookie=function (c_name) {
    if (document.cookie.length>0){
        var c_start=document.cookie.indexOf(c_name + "=")
        if (c_start!=-1){
            c_start=c_start + c_name.length+1
            var c_end=document.cookie.indexOf(";",c_start)
            if (c_end==-1) c_end=document.cookie.length
            return unescape(document.cookie.substring(c_start,c_end))
        }
    }
    return ""
};
window.delCookie=function(name){
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null)
        document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
//获取url请求中的参数
window.GetQueryString=function(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
};
//加载Token
window.loadToken=function () {
    var token = getCookie("token");
    var loginMenu="<div><a href='javascript:void(0);' onclick='goLogin()'>登录</a></div>";
    var loginOutMenu="<div><a href='javascript:void(0);' onclick='loginOut()'>退出</a></div>";
    if(token==null || token==""){
        $("#loginMenu").html(loginMenu);
    }else{
        $("#loginMenu").html(loginOutMenu);
    }
};
//显示弹出窗
dialog.show=function(json){
    var logoType=json.logoType;
    var message=json.message;
    var isCountDown=(json.isCountDown==null?false:json.isCountDown);
    var seconds=(json.seconds==null?3:json.seconds);
    var imgsrc="";
    //判断图片类型
    if(logoType!=null){
        if(logoType==1){
            imgsrc="img/load.gif";
        }else if(logoType==2){
            imgsrc="img/chenggong.png";
        }else if(logoType==3){
            imgsrc="img/shibai.png";
        }
        $("#logo").attr("src",imgsrc);
    }
    //判断提示消息
    if(message!=null){
        $("#message").html(message);
    }
    $(".dialog").show();
    //判断是否开启倒计时
    if(isCountDown){
        $("#isCountDown").html(seconds);
        dialog.interval(seconds,"isCountDown",json.callback);
    }
};
dialog.close=function () {
    $(".dialog").hide();
};
//弹出窗定时
dialog.interval=function(tempSecond,countDownId,callback){
    if($('.dialog').css('display')==='block'){
        var second=tempSecond;
        var time=setInterval (showTime, 1000);
        function showTime(){
            if(second==0){
                if(callback!=null){
                    callback();
                }
                dialog.close();
                clearInterval(time);
            }
            $("#"+countDownId).html('<font>'+second+'</font>');
            second--;
        }
    }
};
//加载商品信息
//加载商品信息
function loadGoodInfo() {
    var token=getCookie("token");
    $.ajax({
        url : ReqUrl.GoodsReqUrl()+"v/queryGoodsById",
        type : "post",
        dataType : "json",
        data : {
            id : goodsId,
            token : token
        },
        success: function(response){
            if(response.code==0){
                var goods=response.data;
                var currentStock=goods.currentStock;
                var goodsName=goods.goodsName;
                var price=goods.price;
                var goodsImg=goods.goodsImg;
                var percent=(goods.stock-currentStock)*100/goods.stock;
                $("#goodsName").html(goodsName);
                $("#goodsImg").attr("src",goodsImg);
                loadPercent(percent+'%',currentStock,'￥'+price,'￥80');
            }
        }
    });
};

function goIndex() {
    window.location.href = ReqUrl.GoodsReqUrl() + "index.html/" + getCookie("token");
}

function goOrderList(){
    window.location.href = ReqUrl.OrdeReqUrl() + "orderList.html/" + getCookie("token");
}

function goLogin(){
    window.location.href = ReqUrl.LoginReqUrl() + "login.html";
}
