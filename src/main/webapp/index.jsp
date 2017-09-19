<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <script src="resources/js/jquery.min.js"></script>
</head>
<body>

<button type="button" id="s" onclick="start();">启动新浪微博定时爬取</button>

<small>每隔2小时爬取一次</small>
<button type="button" id="e" onclick="stop();">停止新浪微博爬取</button>

<textarea name="t" id="t" cols="60" rows="30"></textarea>
<div id="weibo" style="background-color: #fff3ca;border:1px solid  #000000;"></div>


</body>
</html>
<script type="text/javascript">

    function start() {
        $("#s").attr({"disabled":"disabled"});
        $('#s').html("正在爬取..");
        $.ajax({
            url: "/wb/startQuartz",
            type: 'post',
            dataType: "text",
            success: function (data) {

            }
        })
    }
   function stop(){

   }

</script>

