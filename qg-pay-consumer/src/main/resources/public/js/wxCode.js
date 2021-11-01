/***
 * created by 北大课工场
 * 加载订单详细信息
 * @param number
 * @param price
 */
var timer2;
$(function() {
    alert("token存上啦！！！"+getCookie("token"));
    var orderId = GetQueryString("orderId");
    alert("orderId:" + orderId);
    $.ajax({
        url: ReqUrl.PayReqUrl()+"v/reqWxCode",
        method: "post",
        data:{"orderId":orderId,"token":getCookie("token")},
        success: function (data) {
            if(data.code==0){
                new QRCode(document.getElementById('qrcode'), data.data.codeUrl);
                timer2=window.setInterval(function(){
                    flushOrder(orderId)
                },1000);
            }else{
                alert("提示失败");
            }
        }
    });
});
function flushOrder(orderId) {
    $.ajax({
        url: ReqUrl.PayReqUrl()+"v/checkOrderSuccess",
        method: "post",
        data:{"orderId":orderId,"token":getCookie("token")},
        success: function (result) {
            if(result.code==0){
                    window.location.href=ReqUrl.PayReqUrl() + "paySuccess.html?orderId="+orderId + "&token=" + getCookie("token") ;
            }
        }
    });
}

