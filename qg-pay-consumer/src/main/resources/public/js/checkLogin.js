window.checkLogin=function () {
    var token = getCookie("token");
    alert("checkLogin:" + token);
    if(token==null || token==""){
        goLogin();
    }
};
//保存token
function saveToken() {
    var tokenTemp=GetQueryString("token");
    if(tokenTemp!=null && tokenTemp !=""){
        setCookie("token",tokenTemp);
    }
}
//默认启动
$(function () {
    saveToken();
    checkLogin();
});

function loginOut() {
    var token = getCookie("token");
    $.ajax({
        url : ReqUrl.LoginReqUrl()+"v/loginOut",
        type : "post",
        dataType : "json",
        data:{"token":token},
        success: function(response){
            if(response.code==0){
                delCookie("token");
                goLogin();
            }else{
                alert(response.message);
                goLogin();
            }
        }
    });
}