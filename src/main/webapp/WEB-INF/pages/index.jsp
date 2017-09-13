<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>首页</title>
    <script src="/resources/js/jquery.min.js"/>
</head>
<body>

<button type="button" onclick="start();">启动新浪微博爬取</button>

<textarea name="t" id="t" cols="100" rows="60"></textarea>
</body>
</html>
<script type="text/javascript">
function start() {
$.ajax({
url:"/wb/start",
type:'get',
success:function (data) {
$('#t').append(data+"\n");
}
})
}
</script>

