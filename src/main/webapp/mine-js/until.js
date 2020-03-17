

//订阅功能
function subscribe() {
    var email = jQuery.trim($("#email").val());
    //先检查邮件格式对不对，不对就提醒客户
    if (!checkEmail(email)){
        alert("please check your email format");
        return;
    }
    $.ajax({
        url: "/project/foreground/customer/mark",
        type: "put",
        //发送的数据
        data: JSON.stringify({email:email,mark:1}),
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == 1){
                alert("subscribe successful");
            }
            else {
                alert(data.msg);
            }
        }
    })
}
//检查邮箱格式对不对
function checkEmail(email) {
    var myReg = /^[a-zA-Z0-9_-]+@([a-zA-Z0-9]+\.)+(com|cn|net|org)$/;

    if (myReg.test(email)) {
        return true;
    } else {
        return false;
    }
}