
var logout = document.getElementsByClassName("nav-link logout");
logout[0].addEventListener("click",function () {
    $.ajax({
        url: "/project/background/user/session",
        type: "delete",
        //发送的数据
        data: "",
        contentType: "application/json;charset=UTF-8",
        dataType: "json",
        success: function (data) {
            if (data.status == 1) {
                window.location.href = "login.html";
                return;
            } else {
                alert(data.msg);
            }
        }
    })
});