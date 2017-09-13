<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <script src="resources/js/jquery.min.js"></script>
</head>
<body>

<button type="button" id="s" onclick="start();">启动新浪微博爬取</button>

<textarea name="t" id="t" cols="60" rows="30"></textarea>



<div id="weibo" style="background-color: #fff3ca;border:1px solid  #000000;"></div>


</body>
</html>
<script type="text/javascript">
//    $("#weibo").load("./weibo.jsp",function(){ $("#weibo").fadeIn(100);});
    function showWb() {

        $.ajax({
            url:'/wb/getWbPost',
            dataType:'json',
            success:function (data) {
                console.log(data);
                for(var i=0;i<data.length;i++){
                    $('#weibo').append("-------------------------------------------------<br/>");
                    $('#weibo').append(data[i].wbUserTableId+" 发表了 <br/>"+data[i].content+"<br/>"+data[i].time+"<br/>");

                }

                var a=$('a');
                console.log(a);
                for(var i=0;i<a.length;i++){
                    console.log(a[i].href);
                    var local="http://localhost:8088";
                    if(a[i].href.startsWith(local)){
                        var hrefstr=a[i].href;
                       var  newhref=hrefstr.substring(local.length);
                        console.log("之前的"+newhref);
                        a[i].href="https://weibo.cn"+newhref;
                    }
                }
            }
        })


    }

    function start() {
        $("#s").attr({"disabled":"disabled"});
        $('#s').html("正在爬取..");
        $.ajax({
            url: "/wb/start",
            type: 'post',
            dataType: "text",
            success: function (data) {
                $('#t').append(data + "\n");
                $("#s").removeAttr("disabled");//将按钮可用
                $('#s').html("启动新浪微博爬取");
                showWb();
            }
        })
    }
    showWb();

</script>

